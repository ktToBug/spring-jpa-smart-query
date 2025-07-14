package io.github.kttobug.example;

import io.github.kttobug.example.entity.User;
import io.github.kttobug.query.LambdaQueryWrapper;
import io.github.kttobug.spring.SmartQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.List;

@SpringBootTest(classes = SmartQueryExampleApplication.class)
class SmartQueryExampleApplicationTest {

    @Autowired
    private SmartQueryService smartQueryService;

    @Autowired
    private Environment env;

    @Test
    void testLambdaQuery() {
        // 创建查询包装器
        LambdaQueryWrapper<User> queryWrapper = LambdaQueryWrapper.of(User.class)
                .eq(User::getStatus, 1)
                .like(User::getUsername, "admin");
        
        // 使用智能查询服务执行查询
        List<User> list = smartQueryService.findAll(queryWrapper);
        
        System.out.println("Lambda Query Test Result:");
        list.forEach(System.out::println);
        System.out.println("Total count: " + list.size());
        
        // 测试计数功能
        long count = smartQueryService.count(queryWrapper);
        System.out.println("Count query result: " + count);
    }
}
