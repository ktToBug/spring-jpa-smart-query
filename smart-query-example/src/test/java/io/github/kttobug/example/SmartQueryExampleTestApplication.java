package io.github.kttobug.example;

import io.github.kttobug.example.entity.User;
import io.github.kttobug.example.repository.UserRepository;
import io.github.kttobug.query.LambdaQueryWrapper;
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
    void testLambdaQuery() {
        List<User> list = this.userRepository.findAll(
                LambdaQueryWrapper.of(User.class)
                        .eq(User::getStatus, 1)
                        .like(User::getUsername, "admin")
        );
        System.out.println("111");
        list.forEach(System.out::println);
        System.out.println(list.size());
    }
}
