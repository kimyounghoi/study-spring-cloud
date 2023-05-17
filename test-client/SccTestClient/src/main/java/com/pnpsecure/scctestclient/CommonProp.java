package com.pnpsecure.scctestclient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "common")
@RefreshScope
@Setter @Getter @ToString
public class CommonProp {
    private String t1;
    private String t2;
    private String t3;
}
