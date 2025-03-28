package com.connectify.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.connectify.demo.Repository")
@EntityScan(basePackages = "com.connectify.demo.Model")
public class ConnectifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectifyApplication.class, args);
    }

}
