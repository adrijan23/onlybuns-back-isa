package team5.onlybuns;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.service.UserService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    void testConcurrentUserRegistration() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future1 = executor.submit(() -> {
            UserRequest userRequest = new UserRequest();
            userRequest.setUsername("testuser");
            userRequest.setPassword("password1");
            userRequest.setFirstname("John");
            userRequest.setLastname("Doe");
            userRequest.setEmail("test1@example.com");

            userService.saveTransactional(userRequest); // First registration attempt
        });

        Future<?> future2 = executor.submit(() -> {
            try {
                // Simulate a slight delay to ensure the first thread holds the lock
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            UserRequest userRequest = new UserRequest();
            userRequest.setUsername("testuser"); // Same username as the first
            userRequest.setPassword("password2");
            userRequest.setFirstname("Jane");
            userRequest.setLastname("Smith");
            userRequest.setEmail("test2@example.com");

            userService.saveTransactional(userRequest); // Second registration attempt
        });

        // Unwrap the exception and assert the correct type
        Throwable thrown = assertThrows(ExecutionException.class, () -> {
            future1.get(); // First thread should succeed
            future2.get(); // Second thread should throw an exception
        });

        // Verify that the root cause of the exception is the expected one
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertEquals("Transaction conflict: username already in use", thrown.getCause().getMessage());

        executor.shutdown();
    }






}
