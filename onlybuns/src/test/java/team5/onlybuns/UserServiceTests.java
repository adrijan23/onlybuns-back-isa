package team5.onlybuns;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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

    @Autowired
    private CacheManager cacheManager;

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
    @Transactional
    void testConcurrentFollowSameUser() throws Exception {
        // Step 1: two followers and one following user
        User follower1 = userService.findByIdWithFollowing(3L);
        User follower2 = userService.findByIdWithFollowing(2L);
        User followingUser = userService.findByIdWithFollowing(4L);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Step 2: both trying to follow the same user (following)
        Future<?> future1 = executor.submit(() -> {
            try {
                userService.followUser(follower1.getId(), followingUser.getId());
            } catch (RuntimeException e) {
                throw new RuntimeException("First follower failed to follow", e);
            }
        });

        Future<?> future2 = executor.submit(() -> {
            try {
                Thread.sleep(100);
                userService.followUser(follower2.getId(), followingUser.getId());
            } catch (RuntimeException | InterruptedException e) {
                throw new RuntimeException("Second follower failed to follow", e);
            }
        });
        // there wont be any conflicts because of the structure of the database

        future1.get();
        future2.get();

        // updated data
        User updatedFollower1 = userService.findByIdWithFollowing(follower1.getId());
        User updatedFollower2 = userService.findByIdWithFollowing(follower2.getId());
        User updatedFollowing = userService.findByIdWithFollowing(followingUser.getId());

        // follower1 is following 'following'
        assertTrue(updatedFollower1.getFollowing().contains(updatedFollowing));
        // follower2 is following 'following'
        assertTrue(updatedFollower2.getFollowing().contains(updatedFollowing));

        // 'following' has two followers: follower1 and follower2
        assertTrue(updatedFollowing.getFollowers().contains(updatedFollower1));
        assertTrue(updatedFollowing.getFollowers().contains(updatedFollower2));

        executor.shutdown();
    }
//
//    @Test
//    @Transactional
//    void testFollowRateLimit() {
//        Long followerId = 1L;
//        Long followingId = 2L;
//
//        int maxFollowsPerMinute = 5;
//
//        Cache cache = cacheManager.getCache("followLimitCache");
//        cache.clear();
//
//        for (int i = 0; i < maxFollowsPerMinute; i++) {
//            userService.followUser(followerId, followingId);
//        }
//
//        //no exceptions for other followings
//        assertDoesNotThrow(() -> {
//            userService.followUser(followingId, followerId);
//        });
//
//        Throwable thrown = assertThrows(ResponseStatusException.class, () -> {
//            userService.followUser(followerId, followingId);
//        });
//
//        assertEquals(HttpStatus.TOO_MANY_REQUESTS, ((ResponseStatusException) thrown).getStatus());
//        assertTrue(thrown.getMessage().contains("You have exceeded the follow limit"));
//    }


}
