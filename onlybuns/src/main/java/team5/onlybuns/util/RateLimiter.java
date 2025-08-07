package team5.onlybuns.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private final Map<String, Deque<Instant>> userRequests = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS = 5;
    private final Duration TIME_WINDOW = Duration.ofMinutes(1);

    public boolean isAllowed(String userKey) {
        Instant now = Instant.now();
        Deque<Instant> timestamps = userRequests.computeIfAbsent(userKey, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(now.minus(TIME_WINDOW))) {
                timestamps.pollFirst();
            }

            if (timestamps.size() < MAX_REQUESTS) {
                timestamps.addLast(now);
                return true;
            } else {
                return false;
            }
        }
    }
}

