package team5.onlybuns.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import team5.onlybuns.service.UserService;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class BloomFilterConfig {

    @Autowired
    private UserService userService;

    private BloomFilter<String> usernameBloomFilter;

    @PostConstruct
    public void initBloomFilter() {
        usernameBloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                10000, // Expected number of usernames
                0.01   // False positive rate
        );

        // Populate the Bloom filter with existing usernames
        List<String> usernames = userService.findAllUsernames();
        usernames.forEach(username -> usernameBloomFilter.put(username.toLowerCase()));

        System.out.println("Bloom filter initialized and populated. " + usernames.size() + " usernames added.");
    }

    public BloomFilter<String> getUsernameBloomFilter() {
        return usernameBloomFilter;
    }
}

