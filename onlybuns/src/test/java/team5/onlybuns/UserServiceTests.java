package team5.onlybuns;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team5.onlybuns.dto.UserRequest;
import team5.onlybuns.model.User;
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
        // Step 1: two followers and one following user
        UserRequest user1 = new UserRequest();
        user1.setUsername("follower1");
        user1.setPassword("password1");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setEmail("testf1@example.com");
        userService.save(user1);
        User follower1 = userService.findByUsername(user1.getUsername());

        UserRequest user2 = new UserRequest();
        user2.setUsername("follower2");
        user2.setPassword("password1");
        user2.setFirstname("John");
        user2.setLastname("Doe");
        user2.setEmail("testf1@example.com");
        userService.save(user2);
        User follower2 = userService.findByUsername(user2.getUsername());

        UserRequest user3 = new UserRequest();
        user3.setUsername("following");
        user3.setPassword("password1");
        user3.setFirstname("John");
        user3.setLastname("Doe");
        user3.setEmail("testf@example.com");
        userService.save(user3);
        User following = userService.findByUsername(user3.getUsername());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Step 2: both trying to follow the same user (following)
        Future<?> future1 = executor.submit(() -> {
            try {
                userService.followUser(follower1.getId(), following.getId());
            } catch (RuntimeException e) {
                throw new RuntimeException("First follower failed to follow", e);
            }
        });

        Future<?> future2 = executor.submit(() -> {
            try {
                Thread.sleep(100);
                userService.followUser(follower2.getId(), following.getId());
            } catch (RuntimeException | InterruptedException e) {
                throw new RuntimeException("Second follower failed to follow", e);
            }
        });
        // there wont be any conflicts because of the structure of the database

        // both operations should complete without exceptions
        future1.get();  // wait for both tasks to complete
        future2.get();

//        // Step 3: both followers have successfully followed the same user
//        User updatedFollower1 = userService.findByIdWithFollowing(follower1.getId());
//        User updatedFollower2 = userService.findByIdWithFollowing(follower2.getId());
//        User updatedFollowing = userService.findByIdWithFollowing(following.getId());
//
//        // follower1 is following 'following'
//        assertTrue(updatedFollower1.getFollowing().contains(updatedFollowing));
//        // follower2 is following 'following'
//        assertTrue(updatedFollower2.getFollowing().contains(updatedFollowing));
//
//        // 'following' has two followers: follower1 and follower2
//        assertTrue(updatedFollowing.getFollowers().contains(updatedFollower1));
//        assertTrue(updatedFollowing.getFollowers().contains(updatedFollower2));

        executor.shutdown();
    }




}
