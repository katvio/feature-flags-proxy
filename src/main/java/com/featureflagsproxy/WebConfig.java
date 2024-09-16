package com.featureflagsproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                logger.info("Incoming request {} {}", request.getMethod(), request.getRequestURI());
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                if (ex != null) {
                    logger.error("Request {} {} completed with exception", request.getMethod(), request.getRequestURI(), ex);
                } else {
                    logger.info("Request {} {} completed successfully", request.getMethod(), request.getRequestURI());
                }
            }
        });
    }
}
