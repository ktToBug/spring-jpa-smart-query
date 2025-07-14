package io.github.kttobug.spring;

import io.github.kttobug.query.QueryPerformanceMonitor;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(EntityManager.class)
public class LambdaQueryAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public QueryPerformanceMonitor queryPerformanceMonitor() {
        return new QueryPerformanceMonitor();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public SmartQueryService smartQueryService() {
        return new SmartQueryService();
    }
}