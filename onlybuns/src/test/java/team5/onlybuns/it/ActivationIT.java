package team5.onlybuns.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import team5.onlybuns.controller.AuthenticationController;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.util.TokenUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ActivationIT extends ITBase {

    @Autowired AuthenticationController authController;
    @Autowired UserRepository userRepository;

    @MockBean TokenUtils tokenUtils; // da ne zavisimo od realnog JWT parsa

    @Test
    void activateUser_validToken_enablesUser() {
        // seed: user u bazi, disabled
        User u = new User();
        u.setUsername("alice");
        u.setEmail("a@ex.com");
        u.setPassword("enc");
        u.setEnabled(false);
        u = userRepository.save(u);

        when(tokenUtils.getUsernameFromToken("abc")).thenReturn("alice");

        ResponseEntity<?> resp = authController.activateUser("abc");
        assertThat(resp.getStatusCodeValue()).isEqualTo(200);

        User fromDb = userRepository.findById(u.getId()).orElseThrow();
        assertThat(fromDb.isEnabled()).isTrue();
        assertThat(fromDb.getRegistrationDate()).isNotNull();
    }
}
