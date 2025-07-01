package io.github.kttobug.example;

import io.github.kttobug.example.entity.User;
import io.github.kttobug.example.repository.UserRepository;
import io.github.kttobug.spring.LambdaQueryRepositoryFactoryBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
@SpringBootTest
@EntityScan("io.github.kttobug.example.entity")
@EnableJpaRepositories(
        basePackages = "io.github.kttobug.example.repository",
        repositoryFactoryBeanClass = LambdaQueryRepositoryFactoryBean.class
)
class SmartQueryExampleTestApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Environment environment;

    @Test
    void checkEnv() {
        System.out.println("DB_URL = " + environment.getProperty("DB_URL"));
    }

    @Test
    void testLambdaQuery() {
        List<User> activeAdmins = userRepository.findActiveAdmins();
        // 断言...
        activeAdmins.forEach(System.out::println);
    }
} 