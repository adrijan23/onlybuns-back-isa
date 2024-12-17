package team5.onlybuns;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.service.UserService;

import javax.persistence.OptimisticLockException;
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

    @Test
    void testConcurrentFollowSameUser() throws Exception {
        // Step 1: Create and save two followers and one following user
        UserRequest follower1 = new UserRequest();
        follower1.setUsername("follower1");
        follower1.setPassword("password1");
        follower1.setFirstname("John");
        follower1.setLastname("Doe");
        follower1.setEmail("testf1@example.com");

        UserRequest follower2 = new UserRequest();
        follower2.setUsername("follower2");
        follower2.setPassword("password1");
        follower2.setFirstname("John");
        follower2.setLastname("Doe");
        follower2.setEmail("testf1@example.com");

        UserRequest following = new UserRequest();
        following.setUsername("following");
        following.setPassword("password1");
        following.setFirstname("John");
        following.setLastname("Doe");
        following.setEmail("testf@example.com");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Step 2: Simulate two concurrent transactions, both trying to follow the same user (following)
        Future<?> future1 = executor.submit(() -> {
            try {
                userService.followUser(follower1.getId(), following.getId());
            } catch (RuntimeException e) {
                throw new RuntimeException("First follower failed to follow", e);
            }
        });

        Future<?> future2 = executor.submit(() -> {
            try {
                // Simulate a slight delay to ensure some overlap in the operations
                Thread.sleep(100);
                userService.followUser(follower2.getId(), following.getId());
            } catch (RuntimeException | InterruptedException e) {
                throw new RuntimeException("Second follower failed to follow", e);
            }
        });

        // Step 3: Capture and assert that one of the threads encounters an OptimisticLockException
        Throwable thrown = assertThrows(ExecutionException.class, () -> {
            future1.get(); // One follower may succeed
            future2.get(); // The other may fail due to OptimisticLockException
        });

        // Verify that the exception cause is OptimisticLockException
        assertTrue(thrown.getCause().getCause() instanceof OptimisticLockException);

        executor.shutdown();
    }




}
