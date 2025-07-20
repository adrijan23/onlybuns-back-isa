package team5.onlybuns.config;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;
@Configuration
public class ActiveUsersMetricsConfig {
    private final AtomicInteger activeUsers = new AtomicInteger(0);

    @Bean
    public Gauge activeUserGauge(MeterRegistry registry) {
        return Gauge.builder("active_users", activeUsers, AtomicInteger::get)
                .description("Number of active users (JWT-based)")
                .register(registry);
    }

    public AtomicInteger getActiveUsers() {
        return activeUsers;
    }
}
