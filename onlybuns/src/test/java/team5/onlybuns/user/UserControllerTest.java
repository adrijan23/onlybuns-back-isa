package team5.onlybuns.user;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import team5.onlybuns.controller.UserController;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.AddressRepository;
import team5.onlybuns.repository.ImageRepository;
import team5.onlybuns.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController controller;

    @Mock private UserService userService;
    @Mock private ImageRepository imageRepository;
    @Mock private AddressRepository addressRepository;

    @Test
    void updatePassword_badCurrent_returns400_noUpdateCall() {
        // arrange
        Long userId = 7L;
        Principal principal = () -> "bun";

        User current = new User();
        current.setId(userId);
        current.setUsername("bun");
        current.setPassword("ENC"); // simulirani hash u bazi

        when(userService.findByUsername("bun")).thenReturn(current);

        Map<String,String> body = new HashMap<>();
        body.put("currentPassword", "wrong");
        body.put("newPassword", "NewSecret1!");

        when(userService.checkPassword("wrong", "ENC")).thenReturn(false);

        // act
        ResponseEntity<?> resp = controller.updatePassword(userId, body, principal);

        // assert
        assertEquals(400, resp.getStatusCodeValue());
        verify(userService, never()).updatePassword(anyLong(), anyString());
    }

    @Test
    void updatePassword_ok_returns200_andCallsServiceUpdate() {
        // arrange
        Long userId = 8L;
        Principal principal = () -> "alice";

        User current = new User();
        current.setId(userId);
        current.setUsername("alice");
        current.setPassword("HASHED");
        when(userService.findByUsername("alice")).thenReturn(current);

        Map<String,String> body = new HashMap<>();
        body.put("currentPassword", "currPw");
        body.put("newPassword", "BrandNewPw!");

        when(userService.checkPassword("currPw", "HASHED")).thenReturn(true);

        // act
        ResponseEntity<?> resp = controller.updatePassword(userId, body, principal);

        // assert
        assertEquals(200, resp.getStatusCodeValue());
        verify(userService).updatePassword(userId, "BrandNewPw!");
    }
}
