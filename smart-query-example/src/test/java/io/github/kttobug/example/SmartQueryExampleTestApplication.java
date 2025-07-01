package io.github.kttobug.example;

import io.github.kttobug.example.entity.User;
import io.github.kttobug.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.List;

@SpringBootTest(classes = SmartQueryExampleApplication.class)
class SmartQueryExampleApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Environment env;

    @Test
    void debugEnvVars() {
        System.out.println("spring.datasource.url = " + env.getProperty("spring.datasource.url"));
    }

    @Test
    void testLambdaQuery() {
        userRepository.save(new User(5L, "admin", 1));
        List<User> admins = userRepository.findActiveAdmins();
        System.out.println("111");
        admins.forEach(System.out::println);
        System.out.println(admins.size());
        System.out.println("222");
        // 你可以在这里添加断言
    }
}
