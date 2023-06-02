package com.yhkim.study.eurekaclienttest2;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service @RequiredArgsConstructor @Slf4j
public class TestService {
    final LoadBalancerClient loadBalancerClient;
    final RestTemplate restTemplate;
    final RetryRegistry retryRegistry;

    @PostConstruct
    public void init() {
        log.info("==========================================================================================================");
        retryRegistry.getAllRetries().forEach(retry -> retry.getEventPublisher().onRetry(ev -> log.warn("retry - {}", ev)));
    }

    @Retry(name = "callExternalApi", fallbackMethod = "fallbackLoadbalance")
    public ResponseEntity<String> callExternalApi() {
        ServiceInstance instance = loadBalancerClient.choose("YHKIM-EUREKA-CLIENT-A");
        String baseUrl = instance.getUri().toString();
        log.info("load-balancing: {}", baseUrl+"/a/hello");
        return ResponseEntity.ok(restTemplate.getForObject(baseUrl + "/a/hello", String.class) + ", port:"+instance.getPort());
    }
    public ResponseEntity<String> fallbackLoadbalance(Throwable t) {
        log.error(t.getMessage());
        return ResponseEntity.internalServerError().body("Can not connect external api");
    }
}
