package io.github.kttobug.example;

import io.github.kttobug.spring.LambdaQueryRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("io.github.kttobug.example.entity")
@EnableJpaRepositories(
        basePackages = "io.github.kttobug.example.repository",
        repositoryFactoryBeanClass = LambdaQueryRepositoryFactoryBean.class
)
public class SmartQueryExampleApplication {
    // https://chatgpt.com/c/68638fc5-bf84-800c-97d8-0f568bca6cf9
    public static void main(String[] args) {
        SpringApplication.run(SmartQueryExampleApplication.class, args);
    }
}