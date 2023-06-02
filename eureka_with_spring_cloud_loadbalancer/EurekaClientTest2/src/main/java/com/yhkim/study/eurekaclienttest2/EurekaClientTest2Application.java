package com.yhkim.study.eurekaclienttest2;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication @EnableEurekaClient
public class EurekaClientTest2Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientTest2Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
        //return getRestTemplate();
    }

    private RestTemplate getRestTemplate () {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(20);
        connManager.setMaxTotal(30);

        HttpClient client = HttpClientBuilder.create().setConnectionManager(connManager).build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(5000);

        return new RestTemplate(factory);
    }
}
