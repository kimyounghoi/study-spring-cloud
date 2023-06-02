package com.yhkim.study.eurekaclienttest1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${eureka.instance.instance-id}")
    String instanceId;

    @GetMapping("/a/hello")
    public ResponseEntity<String> testC() {
        return ResponseEntity.internalServerError().body("Hello Younghoi Kim, it's eureka");
        //return ResponseEntity.ok("Hello Younghoi Kim, it's eureka - " + instanceId);
    }
}
