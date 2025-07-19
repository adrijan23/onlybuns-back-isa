package team5.onlybuns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import team5.onlybuns.model.User;
import team5.onlybuns.repository.UserRepository;
import team5.onlybuns.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class FollowServiceTest {

    @Autowired
    private UserService followService;

    @Autowired
    private UserRepository userRepository;

    private User testFollower;
    private User testCelebrity;
    private List<User> testUsers;

    @BeforeEach
    @Transactional
    @Rollback(false)  // Don't rollback setup data
    public void setUp() {
        // Clear any existing data
        testUsers = new ArrayList<>();

        // Create test users
        testFollower = createTestUser("testFollower", "follower@test.com", "Test", "Follower");
        testCelebrity = createTestUser("testCelebrity", "celebrity@test.com", "Test", "Celebrity");

        // Create multiple test users for concurrent testing
        for (int i = 0; i < 20; i++) {
            User user = createTestUser("usert" + i, "usert" + i + "@test.com", "User", "Number" + i);
            testUsers.add(user);
        }

        // Explicitly flush to ensure data is persisted
        userRepository.flush();

        System.out.println("Test setup completed. Created " + (testUsers.size() + 2) + " test users.");
        System.out.println("Celebrity ID: " + testCelebrity.getId());
    }

    private User createTestUser(String username, String email, String firstName, String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFollowersCount(0L);
        return userRepository.save(user);
    }

    @Test
    @Transactional  // Keep Hibernate session open during test
    public void testSimpleFollow() {
        // Test basic follow functionality
        assertDoesNotThrow(() -> {
            followService.followUser(testFollower.getId(), testCelebrity.getId());
        });

        // Verify the relationship was created
        User updatedFollower = userRepository.findById(testFollower.getId()).orElseThrow();
        User updatedCelebrity = userRepository.findById(testCelebrity.getId()).orElseThrow();

        assertTrue(updatedFollower.getFollowing().contains(updatedCelebrity));
        assertEquals(1L, updatedCelebrity.getFollowersCount());

        System.out.println("✅ Simple follow test passed");
    }

    @Test
    public void testCannotFollowSelf() {
        // Test that user cannot follow themselves
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followService.followUser(testFollower.getId(), testFollower.getId());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getReason().contains("cannot follow yourself"));

        System.out.println("✅ Cannot follow self test passed");
    }

    @Test
    @Transactional  // Keep session open for lazy loading
    public void testCannotFollowTwice() {
        // First follow should succeed
        followService.followUser(testFollower.getId(), testCelebrity.getId());

        // Second follow should fail
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            followService.followUser(testFollower.getId(), testCelebrity.getId());
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertTrue(exception.getReason().contains("Already following"));

        System.out.println("✅ Cannot follow twice test passed");
    }

    @Test
    public void testConcurrentFollowsSameTarget() throws InterruptedException {
        System.out.println("🧪 Starting concurrent follows test...");

        // Create celebrity for this test specifically
        User celebrityForTest = createTestUser("concurrentCelebrity", "concurrent@test.com", "Concurrent", "Celebrity");

        // Create follower users for this test
        List<User> testFollowers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User follower = createTestUser("concurrentFollower" + i, "cfollower" + i + "@test.com", "Concurrent", "Follower" + i);
            testFollowers.add(follower);
        }

        // Ensure all users are persisted
        userRepository.flush();

        TestTransaction.flagForCommit();   // mark the current (test) tx for commit
        TestTransaction.end();             // actually commit & close it
        TestTransaction.start();           // start a fresh transaction in this thread (so that any @Transactional calls here still work)

        System.out.println("Created celebrity with ID: " + celebrityForTest.getId());
        System.out.println("Created " + testFollowers.size() + " followers");

        int numberOfThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<String> errorMessages = new CopyOnWriteArrayList<>();

        // Submit all tasks
        for (int i = 0; i < numberOfThreads; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    startLatch.await(); // Wait for signal to start
                    Thread.sleep(10 + (index * 5)); // Stagger slightly to increase collision chance

                    followService.followUser(testFollowers.get(index).getId(), celebrityForTest.getId());

                    successCount.incrementAndGet();
                    System.out.println("✅ Thread " + index + " succeeded");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    failCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    errorMessages.add("Thread " + index + ": " + e.getMessage());
                    System.out.println("❌ Thread " + index + " failed: " + e.getMessage());
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // Start all threads simultaneously
        System.out.println("🚀 Starting all threads...");
        startLatch.countDown();

        // Wait for completion (max 30 seconds)
        boolean finished = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(finished, "Test did not complete within timeout");

        // Verify results
        User updatedCelebrity = userRepository.findById(celebrityForTest.getId()).orElseThrow();

        System.out.println("📊 Test Results:");
        System.out.println("   Successful follows: " + successCount.get());
        System.out.println("   Failed follows: " + failCount.get());
        System.out.println("   Final follower count: " + updatedCelebrity.getFollowersCount());

        if (!errorMessages.isEmpty()) {
            System.out.println("   Error messages:");
            errorMessages.forEach(msg -> System.out.println("     " + msg));
        }

        // Assert consistency - most important test!
        assertEquals(successCount.get(), updatedCelebrity.getFollowersCount().intValue(),
                "Follower count should match successful operations");
        assertEquals(numberOfThreads, successCount.get() + failCount.get(),
                "All threads should either succeed or fail");

        executor.shutdown();
        System.out.println("✅ Concurrent follows test passed - data consistency maintained!");
    }

    @Test
    public void testRateLimiting() throws InterruptedException {
        System.out.println("🧪 Starting rate limiting test...");

        User rateLimitUser = createTestUser("concurrentCelebrity", "concurrent@test.com", "Concurrent", "Celebrity");

        // Create follower users for this test
        List<User> targetUsers = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            User follower = createTestUser("concurrentFollower" + i, "cfollower" + i + "@test.com", "Concurrent", "Follower" + i);
            targetUsers.add(follower);
        }

        // Ensure all users are persisted
        userRepository.flush();

        TestTransaction.flagForCommit();   // mark the current (test) tx for commit
        TestTransaction.end();             // actually commit & close it
        TestTransaction.start();           // start a fresh transaction in this thread (so that any @Transactional calls here still work)

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger rateLimitedCount = new AtomicInteger(0);
        AtomicInteger otherErrors = new AtomicInteger(0);

        // Try to follow many users rapidly (more than the 50 per minute limit)
        for (int i = 0; i < 60; i++) {
            try {
                followService.followUser(rateLimitUser.getId(), targetUsers.get(i).getId());
                successCount.incrementAndGet();

                if (i < 49) {
                    System.out.println("✅ Follow " + (i + 1) + " succeeded");
                } else {
                    System.out.println("⚠️ Follow " + (i + 1) + " succeeded (unexpected after limit)");
                }

            } catch (ResponseStatusException e) {
                if (e.getStatus() == HttpStatus.TOO_MANY_REQUESTS) {
                    rateLimitedCount.incrementAndGet();
                    if (rateLimitedCount.get() <= 3) { // Only log first few
                        System.out.println("🚫 Follow " + (i + 1) + " rate limited");
                    }
                } else {
                    otherErrors.incrementAndGet();
                    System.out.println("❌ Follow " + (i + 1) + " failed with other error: " + e.getMessage());
                }
            } catch (Exception e) {
                otherErrors.incrementAndGet();
                System.out.println("❌ Follow " + (i + 1) + " failed with exception: " + e.getMessage());
            }
        }

        System.out.println("📊 Rate Limiting Results:");
        System.out.println("   Successful follows: " + successCount.get());
        System.out.println("   Rate limited: " + rateLimitedCount.get());
        System.out.println("   Other errors: " + otherErrors.get());

        // Verify rate limiting worked
        assertEquals(50, successCount.get(), "Should allow exactly 50 follows per minute");
        assertEquals(10, rateLimitedCount.get(), "Should rate limit 10 requests");
        assertEquals(0, otherErrors.get(), "Should not have other errors");

        System.out.println("✅ Rate limiting test passed!");
    }

    @Test
    public void testOptimisticLockHandling() throws InterruptedException {
        System.out.println("🧪 Starting optimistic lock test...");

        User optimisticTestUser = createTestUser("optimisticUser", "optimistic@test.com", "Optimistic", "User");
        User targetUser = createTestUser("optimisticTarget", "target@test.com", "Target", "User");
        TestTransaction.flagForCommit();   // mark the current (test) tx for commit
        TestTransaction.end();             // actually commit & close it
        TestTransaction.start();           // start a fresh transaction in this thread (so that any @Transactional calls here still work)

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger optimisticExceptions = new AtomicInteger(0);
        AtomicInteger successCount = new AtomicInteger(0);

        // Thread 1: Modify the follower user to cause version conflict
        CompletableFuture<Void> conflictThread = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(50); // Let the follow operation start first

                // Modify the user in a separate transaction to increment version
                User userToModify = userRepository.findById(optimisticTestUser.getId()).orElseThrow();
                userToModify.setFirstName("ModifiedConcurrently");
                userRepository.save(userToModify);

                System.out.println("🔄 Concurrent modification completed");

            } catch (Exception e) {
                System.out.println("❌ Conflict thread error: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // Thread 2: Try to follow (might encounter optimistic lock exception)
        CompletableFuture<Void> followThread = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(25); // Start the follow operation
                followService.followUser(optimisticTestUser.getId(), targetUser.getId());
                successCount.incrementAndGet();
                System.out.println("✅ Follow operation succeeded");

            } catch (ResponseStatusException e) {
                if (e.getReason() != null && e.getReason().contains("Another operation is in progress")) {
                    optimisticExceptions.incrementAndGet();
                    System.out.println("🔒 Optimistic lock exception handled correctly");
                } else {
                    System.out.println("❌ Unexpected ResponseStatusException: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("❌ Follow thread error: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // Wait for both threads to complete
        boolean finished = latch.await(10, TimeUnit.SECONDS);
        assertTrue(finished, "Optimistic lock test did not complete within timeout");

        System.out.println("📊 Optimistic Lock Results:");
        System.out.println("   Successful follows: " + successCount.get());
        System.out.println("   Optimistic lock exceptions: " + optimisticExceptions.get());

        // Either the follow succeeded OR we got an optimistic lock exception
        assertTrue(successCount.get() == 1 || optimisticExceptions.get() >= 1,
                "Either follow should succeed or optimistic lock exception should occur");

        System.out.println("✅ Optimistic lock handling test passed!");
    }

    @Test
    public void testTransactionRollback() {
        System.out.println("🧪 Starting transaction rollback test...");

        User follower = createTestUser("rollbackFollower", "rollback@test.com", "Rollback", "User");
        User target = createTestUser("rollbackTarget", "target@test.com", "Target", "User");

        // Record initial state
        Long initialFollowerCount = target.getFollowersCount();
        int initialFollowingSize = follower.getFollowing().size();
        int initialFollowersSize = target.getFollowers().size();

        System.out.println("📊 Initial state:");
        System.out.println("   Target followers count: " + initialFollowerCount);
        System.out.println("   Follower following size: " + initialFollowingSize);
        System.out.println("   Target followers size: " + initialFollowersSize);

        // Modify target user in separate transaction to create version conflict
        User targetInSeparateTransaction = userRepository.findById(target.getId()).orElseThrow();
        targetInSeparateTransaction.setFirstName("ModifiedExternally");
        userRepository.save(targetInSeparateTransaction);

        // Try to follow - should cause rollback due to version conflict
        try {
            followService.followUser(follower.getId(), target.getId());
            System.out.println("⚠️ Follow unexpectedly succeeded");
        } catch (ResponseStatusException e) {
            System.out.println("🔒 Follow failed as expected: " + e.getReason());
        }

        // Verify rollback - all changes should be undone
        User finalFollower = userRepository.findById(follower.getId()).orElseThrow();
        User finalTarget = userRepository.findById(target.getId()).orElseThrow();

        System.out.println("📊 Final state:");
        System.out.println("   Target followers count: " + finalTarget.getFollowersCount());
        System.out.println("   Follower following size: " + finalFollower.getFollowing().size());
        System.out.println("   Target followers size: " + finalTarget.getFollowers().size());

        // Assert rollback occurred
        assertEquals(initialFollowingSize, finalFollower.getFollowing().size(),
                "Follower's following list should be unchanged after rollback");
        assertEquals(initialFollowersSize, finalTarget.getFollowers().size(),
                "Target's followers list should be unchanged after rollback");
        // Note: followersCount might have changed due to our external modification

        System.out.println("✅ Transaction rollback test passed!");
    }

    @Test
    public void testFollowWithThreadSleep() throws InterruptedException {
        System.out.println("🧪 Starting thread sleep test (manual timing test)...");

        User sleepTarget = createTestUser("sleepTarget", "sleep@test.com", "Sleep", "Target");
        User sleepFollower1 = createTestUser("sleepFollower1", "follower1@test.com", "Follower", "One");
        User sleepFollower2 = createTestUser("sleepFollower2", "follower2@test.com", "Follower", "Two");

        // Note: You can add Thread.sleep() in your FollowService.followUser() method for testing:
        // if (followerId.equals(sleepFollower1.getId())) {
        //     Thread.sleep(2000); // Make first thread slower
        // }

        TestTransaction.flagForCommit();   // mark the current (test) tx for commit
        TestTransaction.end();             // actually commit & close it
        TestTransaction.start();           // start a fresh transaction in this thread (so that any @Transactional calls here still work)


        long startTime = System.currentTimeMillis();

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("🔵 Thread 1 starting follow...");
                System.out.println("follower 1 id: "+sleepFollower1.getId());
                followService.followUser(sleepFollower1.getId(), sleepTarget.getId());
                return "Thread 1 SUCCESS";
            } catch (Exception e) {
                return "Thread 1 FAILED: " + e.getMessage();
            }
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100); // Start slightly later
                System.out.println("🔴 Thread 2 starting follow...");
                followService.followUser(sleepFollower2.getId(), sleepTarget.getId());
                return "Thread 2 SUCCESS";
            } catch (Exception e) {
                return "Thread 2 FAILED: " + e.getMessage();
            }
        });

        // Wait for both to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2);
        allOf.join();

        long endTime = System.currentTimeMillis();

        System.out.println("📊 Thread Sleep Test Results:");
        System.out.println("   " + future1.join());
        System.out.println("   " + future2.join());
        System.out.println("   Total time: " + (endTime - startTime) + "ms");

        // Verify final state
        User finalTarget = userRepository.findById(sleepTarget.getId()).orElseThrow();
        System.out.println("   Final followers count: " + finalTarget.getFollowersCount());
        System.out.println("   Final followers size: " + finalTarget.getFollowers().size());

        assertEquals(2L, finalTarget.getFollowersCount().longValue());
        assertEquals(2, finalTarget.getFollowers().size());

        System.out.println("✅ Thread sleep test passed!");
    }

    @Test
    public void testNonExistentUsers() {
        System.out.println("🧪 Starting non-existent users test...");

        // Test following non-existent user
        ResponseStatusException exception1 = assertThrows(ResponseStatusException.class, () -> {
            followService.followUser(testFollower.getId(), 99999L);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception1.getStatus());

        // Test non-existent follower
        ResponseStatusException exception2 = assertThrows(ResponseStatusException.class, () -> {
            followService.followUser(99999L, testCelebrity.getId());
        });
        assertEquals(HttpStatus.NOT_FOUND, exception2.getStatus());

        System.out.println("✅ Non-existent users test passed!");
    }
}