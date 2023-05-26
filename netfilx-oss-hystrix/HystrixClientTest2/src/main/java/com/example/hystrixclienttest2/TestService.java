package com.example.hystrixclienttest2;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service @RequiredArgsConstructor @Slf4j
public class TestService {
    final RestTemplate rest;

    @HystrixCommand(
            fallbackMethod = "defaultProductPrice",
        commandProperties = {
                // 해당 시간 동안 메서드가 끝나지 않으면 circuit open
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),

                // 성공/실패 통계 집계 시간 (default 10s)
                @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),

                // circuit open 여부를 판단할 최소 요청 수 (default 20)
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "1"),

                // circuit open 여부를 판단할 실패률  (default 50%)
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),

                // circuit open 지속 시간 (default 5s)
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
        }
        // 위 설정에 대한 설명
        // 이 메서드가 2초 이상 걸리면 circuit open
        // 5초 동안 메서드의 성공/실패를 집계하며 최소 2번의 요청은 있어야 한다.
        // 2번 이상 요청이 오는데 1번 이상 (50%) 실패할 경우 5초간 fallback method 를 수행한다.
    )
    public String getProductPrice(String productId) {
        return rest.getForObject("http://localhost:8080/product/"+productId, String.class);
    }
    public String defaultProductPrice(String productId, Throwable t) {
        log.error("default fallback method error ==> {}", t.getMessage());
        return "product ("+productId+")'s price is zero";
    }





    @HystrixCommand(
            fallbackMethod = "defaultProductPrice",
            commandProperties = {
                    // 해당 시간 동안 메서드가 끝나지 않으면 circuit open
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),

                    // 성공/실패 통계 집계 시간 (default 10s)
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),

                    // circuit open 여부를 판단할 최소 요청 수 (default 20)
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "1"),

                    // circuit open 여부를 판단할 실패률  (default 50%)
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),

                    // circuit open 지속 시간 (default 5s)
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            }
            // 위 설정에 대한 설명
            // 이 메서드가 2초 이상 걸리면 circuit open
            // 5초 동안 메서드의 성공/실패를 집계하며 최소 2번의 요청은 있어야 한다.
            // 2번 이상 요청이 오는데 1번 이상 (50%) 실패할 경우 5초간 fallback method 를 수행한다.
    )
    public String getSecondMethod(String productId) {
        return rest.getForObject("http://localhost:8080/product/"+productId, String.class);
    }
}
