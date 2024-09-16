package com.featureflagsproxy;

import com.github.benmanes.caffeine.cache.Cache;
import com.launchdarkly.sdk.LDContext;
import com.launchdarkly.sdk.server.LDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProxyController {

    private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

    @Autowired
    private LDClient ldClient;

    @Autowired
    private Cache<String, Boolean> featureFlagCache;

    @GetMapping("/feature/{featureKey}")
    public Boolean getFeatureStatus(@PathVariable String featureKey,
                                    HttpServletRequest request,
                                    @RequestParam(value = "microserviceId", required = false) String microserviceId) {
        String clientIp = request.getRemoteAddr();
        logger.info("Request from IP: {}, Microservice ID: {}", clientIp, microserviceId);

        // Setup context, ideally, this should be dynamic based on actual request details
        LDContext context = LDContext.builder("example-user-key").build();
        logger.debug("Using context: {}", context);

        try {
            // Check cache first
            Boolean cachedValue = featureFlagCache.getIfPresent(featureKey);
            if (cachedValue != null) {
                logger.info("Cache hit for feature flag '{}': {}", featureKey, cachedValue);
                return cachedValue;
            }

            // If not in cache, evaluate feature flag and cache the result
            boolean flagValue = ldClient.boolVariation(featureKey, context, false);
            featureFlagCache.put(featureKey, flagValue);
            logger.info("Feature flag '{}' evaluated to: {}", featureKey, flagValue);
            return flagValue;
        } catch (Exception e) {
            logger.error("Error evaluating feature flag '{}': {}", featureKey, e.getMessage(), e);
            return false; // Optionally handle this more gracefully
        }
    }
}
