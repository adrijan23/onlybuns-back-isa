package team5.onlybuns.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import team5.onlybuns.config.BloomFilterConfig;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.EmailService;
import team5.onlybuns.service.impl.EmailServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("it")
class AuthRegistrationIT extends ITBase {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired UserRepository userRepository;
    @Autowired BloomFilterConfig bloomFilterConfig;

    @MockBean
    EmailServiceImpl emailService; // spriječimo realno slanje

    @Test
    void signup_happyPath_persistsUser_andSendsActivationEmail() throws Exception {
        // given
        UserRequest req = new UserRequest();
        req.setUsername("ituser");
        req.setEmail("ituser@example.com");
        req.setPassword("Secret123!");
        req.setFirstname("IT");
        req.setLastname("User");

        // when
        mvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // then: user upisan u bazu
        User fromDb = userRepository.findByUsername("ituser");
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getEmail()).isEqualTo("ituser@example.com");
        assertThat(fromDb.isEnabled()).isFalse(); // kod tebe je false do aktivacije

        // email poslat
        verify(emailService).sendEmail(eq("ituser@example.com"), contains("Activation"), contains("http://"));
    }

    @Test
    void signup_conflict_whenUsernameExists_andBloomHits() throws Exception {
        // seed: postoji user u bazi
        User exists = new User();
        exists.setUsername("exists");
        exists.setEmail("x@x.com");
        exists.setPassword("enc");
        userRepository.save(exists);

        // ubacimo i u Bloom da bi kontroler vratio 409
        bloomFilterConfig.getUsernameBloomFilter().put("exists");

        UserRequest req = new UserRequest();
        req.setUsername("exists");
        req.setEmail("new@x.com");
        req.setPassword("Secret123!");

        mvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict());

        verify(emailService, never()).sendEmail(any(), any(), any());
    }
}
