package team5.onlybuns.auth;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import team5.onlybuns.config.ActiveUsersMetricsConfig;
import team5.onlybuns.config.BloomFilterConfig;
import team5.onlybuns.controller.AuthenticationController;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.User;
import team5.onlybuns.service.EmailService;
import team5.onlybuns.service.UserService;
import team5.onlybuns.util.TokenUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController controller;

    @Mock private TokenUtils tokenUtils;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserService userService;
    @Mock private EmailService emailService;
    @Mock private CacheManager cacheManager;
    @Mock private BloomFilterConfig bloomFilterConfig;
    @Mock private ActiveUsersMetricsConfig metricsConfig;

    @Test
    void registerUser_ok_withValidData_returns201_andSendsActivationEmail() {
        // arrange
        UserRequest req = new UserRequest();
        req.setUsername("BunLover");               // mixed case da proverimo normalizaciju
        req.setEmail("bun@example.com");

        // Realni BloomFilter (bez mokovanja final klase)
        BloomFilter<String> realBloom = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                10_000,        // očekivan broj elemenata
                0.01           // false positive rate
        );
        when(bloomFilterConfig.getUsernameBloomFilter()).thenReturn(realBloom);

        User saved = new User();
        saved.setUsername("BunLover");
        when(userService.saveTransactional(req)).thenReturn(saved);

        when(tokenUtils.generateToken("BunLover")).thenReturn("tok123");

        // precondition: pre registracije Bloom ne sadrži username
        assertFalse(realBloom.mightContain("bunlover"));

        // act
        ResponseEntity<?> resp = controller.registerUser(req);

        // assert
        assertEquals(201, resp.getStatusCodeValue(), "Expected HTTP 201 Created");
        verify(userService).saveTransactional(req);
        verify(emailService).sendEmail(eq("bun@example.com"), contains("Activation"), contains("tok123"));

        // posle registracije očekujemo da je username upisan u BloomFilter
        assertTrue(realBloom.mightContain("bunlover"));

        // nema sporednih interakcija koje ne očekujemo
        verifyNoInteractions(authenticationManager, cacheManager);
    }
}
