package com.yhkim.study.resilience4jtest;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController @RequiredArgsConstructor @Slf4j
public class TestController {

    final TestService service;

//    @RateLimiter(name = "getProductPrice", fallbackMethod = "failedGetProductPriceRl")
    @GetMapping("/product/{productId}")
    public ResponseEntity<String> getProductInfo(@PathVariable String productId) {
        return ResponseEntity.ok(service.getProductPrice(productId));
    }
    public ResponseEntity<String> failedGetProductPriceRl(String productId, Throwable t) {
        log.error("[RateLimit] Failed get product price");
        return ResponseEntity.internalServerError().body("잠시 후 다시 시도해 주시기 바랍니다. ");
    }

    @TimeLimiter(name = "getProductPrice", fallbackMethod = "failedGetProductPriceTl")
    @GetMapping("/product/time/{productId}")
    public CompletableFuture<ResponseEntity<String>> getProductInfoTime(@PathVariable String productId) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(service.getProductPrice(productId)));
    }
    public CompletableFuture<ResponseEntity<String>> failedGetProductPriceTl(String productId, Throwable t) {
        log.error("[TimeLimit] Failed get product price");
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("시간초과"));
    }

    @GetMapping("/product/bulk/{productId}")
    public ResponseEntity<String> getProductInfoBulk(@PathVariable String productId) {
        return service.getProductPrice();
    }
}
