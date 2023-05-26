package com.yhkim.study.resilience4jtest;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration @RequiredArgsConstructor
public class RateLimiterConf {
    final RateLimiterRegistry rateLimiterRegistry;

    @PostConstruct
    public void init() {
        rateLimiterRegistry.rateLimiter("geProductPrice").executeRunnable(() -> {

        });
        rateLimiterRegistry.rateLimiter("geProductPrice2");

    }
}
