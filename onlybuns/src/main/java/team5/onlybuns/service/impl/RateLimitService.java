package team5.onlybuns.service.impl;

//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
//    private final RedisTemplate<String, String> redisTemplate;
//    private static final int MAX_FOLLOWS_PER_MINUTE = 50;
//    public RateLimitService(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public boolean checkRateLimit(Long userId){
//        String key = "follow_limit:"+userId;
//        String currentMinute = String.valueOf(System.currentTimeMillis()/60000);
//        String rateLimitKey = key+":"+currentMinute;
//
//        try{
//            Long currentCount = redisTemplate.opsForValue().increment(rateLimitKey);
//
//            if(currentCount == 1){
//                redisTemplate.expire(rateLimitKey, Duration.ofMinutes(1));
//            }
//
//            return currentCount <= MAX_FOLLOWS_PER_MINUTE;
//        } catch(Exception e){
//            System.out.println("Rate limiting failed, allowing operation: "+e.getMessage());
//            return false;
//        }
//    }
}
