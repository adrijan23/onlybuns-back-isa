package team5.onlybuns.user;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.Role;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.RoleService;
import team5.onlybuns.service.impl.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService; // mora konkretna impl, ne interfejs

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    // ➜ NOVO: mock za RoleService da izbegnemo NPE
    @Mock private RoleService roleService;

    @Test
    void saveTransactional_mapsFields_andCallsRepositorySave() {
        // arrange
        UserRequest req = new UserRequest();
        req.setUsername("BunLover");
        req.setEmail("bun@example.com");
        req.setPassword("Secret123!");

        // ako servis enkoduje lozinku:
        when(passwordEncoder.encode("Secret123!")).thenReturn("HASH");

        // ako servis dodeljuje default rolu (npr. "ROLE_USER"):
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        List<Role> roles = List.of(roleUser);
        when(roleService.findByName("ROLE_USER")).thenReturn(roles);

        // stub repo.save da vrati entitet sa ID-jem (kao JPA)
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(42L);
            return u;
        });

        // act
        User out = userService.saveTransactional(req);

        // assert
        assertNotNull(out);
        assertEquals(42L, out.getId());

        // proveri šta je tačno poslato u repo.save
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertEquals("BunLover", saved.getUsername());
        assertEquals("bun@example.com", saved.getEmail());
        assertEquals("HASH", saved.getPassword());   // potvrda encode-a
        verify(userRepository).findByUsernameWithLock("BunLover"); // provera lock/check duplikata
        verify(roleService).findByName("ROLE_USER");               // dodela default role


    }


    @Test
    void followUser_happyPath_addsRelation_incrementsFollowers_savesBoth() {
        Long followerId = 10L, followingId = 20L;

        // follower ima prazan following skup
        User follower = new User();
        follower.setId(followerId);
        follower.setFollowing(new HashSet<>());

        // following postoji i ima početni followersCount=0
        User following = new User();
        following.setId(followingId);
        following.setFollowersCount(0L);

        // repo nalazi oba korisnika (isti instance koje koristimo u asertima)
        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findByIdWithLock(followingId)).thenReturn(Optional.of(following));

        // act + assert: ne baca izuzetak
        assertDoesNotThrow(() -> userService.followUser(followerId, followingId));

        // following je dodat u follower.following
        assert(follower.getFollowing().contains(following));

        // followersCount target korisnika je uvećan
        assertEquals(1, following.getFollowersCount());

        // sačuvana su oba entiteta
        verify(userRepository).save(follower);
        verify(userRepository).save(following);

        // nema neželjenih dodatnih save-ova
        verify(userRepository, times(2)).save(any(User.class));
    }

}
