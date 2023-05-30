package com.yhkim.study.eurekaclienttest2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController @RequiredArgsConstructor @Slf4j
public class TestController {

    final RestTemplate restTemplate;
    final DiscoveryClient discoveryClient;
    final LoadBalancerClient loadBalancerClient;

    @GetMapping("/b/hello")
    public ResponseEntity<String> hello() {
        String baseUrl = "";
        List<ServiceInstance> list = discoveryClient.getInstances("YHKIM-EUREKA-CLIENT-A");
        for (ServiceInstance instance : list) {
            log.info("{}, {}", instance.toString(), instance.getMetadata().toString());
        }
        if (!CollectionUtils.isEmpty(list)) {
            baseUrl = list.get(0).getUri().toString();
        }
        return ResponseEntity.ok(restTemplate.getForObject(baseUrl + "/a/hello", String.class));
    }

    @GetMapping("/c/hello")
    public ResponseEntity<String> helloWithLb() {
        String baseUrl = loadBalancerClient.choose("YHKIM-EUREKA-CLIENT-A").getUri().toString();
        log.info("load-balancing: {}", baseUrl+"/a/hello");
        return ResponseEntity.ok(restTemplate.getForObject(baseUrl + "/a/hello", String.class));
    }

}

