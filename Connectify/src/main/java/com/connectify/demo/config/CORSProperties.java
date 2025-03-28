package com.connectify.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class CORSProperties {
    private String[] allowedOrigins;
    private String[] allowedHeaders;
    private String[] allowedMethods;
}