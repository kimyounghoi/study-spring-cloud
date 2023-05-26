package com.yhkim.study.hystrixdashboardtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication @EnableHystrixDashboard
public class HystrixDashboardTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardTestApplication.class, args);
    }

}
