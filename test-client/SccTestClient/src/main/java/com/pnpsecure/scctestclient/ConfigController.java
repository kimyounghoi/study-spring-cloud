package com.pnpsecure.scctestclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor @Slf4j
public class ConfigController {
    private final PrivateProp privateProp;

    @GetMapping("/private-config")
    public ResponseEntity<String> privateConfig() {
        log.info("private properties: {}", privateProp.toString());
        return ResponseEntity.ok(privateProp.toString());
    }

    private final CommonProp commonProp;
    @GetMapping("/common-config")
    public ResponseEntity<String> commonConfig() {
        log.info("common properties: {}", commonProp.toString());
        return ResponseEntity.ok(commonProp.toString());
    }

}
