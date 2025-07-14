package io.github.kttobug.example;

import io.github.kttobug.spring.EnableSmartJpa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSmartJpa
public class SmartQueryExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartQueryExampleApplication.class, args);
    }
}
