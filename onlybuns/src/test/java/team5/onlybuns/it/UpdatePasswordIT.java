// src/test/java/team5/onlybuns/it/UpdatePasswordMvcIT.java
package team5.onlybuns.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdatePasswordMvcIT extends ITBase {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    void updatePassword_happyPath_MockMvc_returns200_andUpdatesHash() throws Exception {
        // seed user u bazi sa poznatim hashom
        User u = new User();
        u.setUsername("bob");
        u.setEmail("b@ex.com");
        u.setPassword(passwordEncoder.encode("currPw"));
        u = userRepository.save(u);

        // telo zahteva
        Map<String,String> body = Map.of(
                "currentPassword", "currPw",
                "newPassword", "newPw1!"
        );

        // PUT /api/user/{id}/update-password
        mvc.perform(
                        put("/api/user/{userId}/update-password", u.getId())
                                .with(user("bob").roles("USER"))  // prolazi @PreAuthorize("hasAnyRole('ADMIN','USER')")
                                .with(csrf())                      // jer je PUT
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(body))
                )
                .andExpect(status().isOk());

        // assert u bazi
        User fromDb = userRepository.findById(u.getId()).orElseThrow();
        assertThat(passwordEncoder.matches("newPw1!", fromDb.getPassword())).isTrue();
    }
}
