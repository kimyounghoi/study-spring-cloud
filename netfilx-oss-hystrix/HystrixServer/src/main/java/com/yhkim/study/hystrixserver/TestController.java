package com.yhkim.study.hystrixserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController @Slf4j
public class TestController {
    Random rand = new Random();

    @GetMapping("/product/{productId}")
    public ResponseEntity<String> getProductPrice(@PathVariable String productId) {
        log.info("Received request ");
        try {
                Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if (rand.nextBoolean()) {
//            log.error("Failed");
//            throw new RuntimeException("test exception in other service");
//        }
        log.info("Success");
        return ResponseEntity.ok(productId + "'s price is "+rand.nextInt());
    }
}
