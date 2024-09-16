package com.featureflagsproxy;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    private static final long DEFAULT_TTL = 2; // Default TTL of 2 minutes 

    @Bean
    public Cache<String, Boolean> featureFlagCache() {
        long ttl = DEFAULT_TTL;
        String ttlEnv = System.getenv("CACHE_TTL_MINUTES");
        if (ttlEnv != null) {
            try {
                ttl = Long.parseLong(ttlEnv);
            } catch (NumberFormatException e) {
                // Use default TTL if parsing fails
            }
        }
        return Caffeine.newBuilder()
                .expireAfterWrite(ttl, TimeUnit.MINUTES)
                .build();
    }
}
