package io.github.kttobug.spring;

import io.github.kttobug.spring.aspect.LambdaQueryExecutorAspect;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ConditionalOnClass(EntityManager.class)
// @EnableJpaRepositories(repositoryFactoryBeanClass = LambdaQueryRepositoryFactoryBean.class)
public class LambdaQueryAutoConfiguration {
    @Bean
    public LambdaQueryExecutorAspect lambdaQueryExecutorAspect() {
        return new LambdaQueryExecutorAspect();
    }
}