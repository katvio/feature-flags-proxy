package com.featureflagsproxy;

import com.launchdarkly.sdk.server.LDClient;
import com.launchdarkly.sdk.server.LDConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FeatureFlagsProxyApplication {

    private static final String SDK_KEY = System.getenv("LAUNCHDARKLY_SDK_KEY");

    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagsProxyApplication.class, args);
    }

    @Bean
    public LDClient ldClient() {
        if (SDK_KEY == null || SDK_KEY.trim().isEmpty()) {
            throw new IllegalStateException("LAUNCHDARKLY_SDK_KEY is not set");
        }
        LDConfig config = new LDConfig.Builder().build();
        return new LDClient(SDK_KEY, config);
    }
}
