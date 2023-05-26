package com.example.hystrixclienttest2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor @Slf4j
public class TestController {

    private final TestService service;

    @GetMapping("/product/{productId}")
    public String test(@PathVariable String productId) {
        return service.getProductPrice(productId);
    }

    @GetMapping("/product/second/{productId}")
    public String test2(@PathVariable String productId) {
        return service.getSecondMethod(productId);
    }
}
