package com.yhkim.study.resilience4jtest;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service @RequiredArgsConstructor @Slf4j
public class TestService {

    final RestTemplate rest;
    final RetryRegistry retryRegistry;
    final CircuitBreakerRegistry circuitRegistry;
    final RateLimiterRegistry rateLimiterRegistry;
    final TimeLimiterRegistry timeLimiterRegistry;

    @PostConstruct
    public void init() {
        retryRegistry.getAllRetries().forEach(retry -> retry.getEventPublisher().onRetry(ev -> log.warn("retry - {}", ev)));
        circuitRegistry.getAllCircuitBreakers().forEach(breaker -> breaker.getEventPublisher().onEvent(ev -> log.warn("circuit - {}", ev)));
        rateLimiterRegistry.getAllRateLimiters().forEach(limiter -> limiter.getEventPublisher().onEvent(ev -> log.warn("rate - {}", ev)));
//        for (io.github.resilience4j.ratelimiter.RateLimiter rateLimiter : rateLimiterRegistry.getAllRateLimiters()) {
//            log.info("rate limiter - name: {}", rateLimiter.getName());
//            log.info("rate limiter - limit for period: {}", rateLimiter.getRateLimiterConfig().getLimitForPeriod());
//            log.info("rate limiter - timeout duration: {}", rateLimiter.getRateLimiterConfig().getTimeoutDuration());
//            log.info("rate limiter - refresh period: {}", rateLimiter.getRateLimiterConfig().getLimitRefreshPeriod());
//        }
//        timeLimiterRegistry.getAllTimeLimiters().forEach(timer -> timer.getEventPublisher().onEvent(ev -> log.warn("time - {}", ev)));

    }

    @CircuitBreaker(name = "getProductPrice", fallbackMethod = "failedGetProductPriceCb")
    @Retry(name = "getProductPrice", fallbackMethod = "failedGetProductPriceRt")
//    @RateLimiter(name = "getProductPrice", fallbackMethod = "failedGetProductPriceRl")
    public String getProductPrice(String productId) {
        return "bulk head test";
//        String rtn = rest.getForObject("http://localhost:8080/product/" + productId, String.class);
//        log.info("Success get product price");
//        return rtn;
        /*
        AtomicReference<String> rtn = new AtomicReference<>();
        retryRegistry.retry("getProductPrice").executeRunnable(() -> {
            rtn.set(rest.getForObject("http://localhost:8080/product/" + productId, String.class));
            log.info("Success get product price");
        });
        return rtn.get();
         */
    }

    @Bulkhead(name = "bulkTest", fallbackMethod = "failedGetProductPriceBk")
    public ResponseEntity<String> getProductPrice() {
        return new ResponseEntity<>(rest.getForObject("http://localhost:8080/product/123", String.class), HttpStatus.OK);
    }
    public String failedGetProductPriceCb(String productId, Throwable t) {
        log.error("[CircuitBreaker] Failed get product price");
        return "sold out";
    }
    public String failedGetProductPriceRt(String productId, Throwable t) {
        log.error("[Retry] Failed get product price");
        return "sold out";
    }
    public String failedGetProductPriceRl(String productId, Throwable t) {
        log.error("[RateLimit] Failed get product price");
        return "forbidden";
    }
    public String failedGetProductPriceTl(String productId, Throwable t) {
        log.error("[TimeLimit] Failed get product price");
        return "forbidden";
    }
    public ResponseEntity<String> failedGetProductPriceBk(Throwable t) {
        log.error("[Bulkhead] Failed get product price");
        return new ResponseEntity<>( "bulk error", HttpStatus.CONFLICT);
    }

}
