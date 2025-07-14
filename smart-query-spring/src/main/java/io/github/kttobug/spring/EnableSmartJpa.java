package io.github.kttobug.spring;

import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

/**
 * 启用Smart JPA查询功能
 * 
 * <p>该注解用于开启Spring JPA Smart Query功能，包括自动配置和JPA仓库支持。
 * 只需在启动类上添加此注解即可使用所有功能。</p>
 * 
 * @author kttobug
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LambdaQueryAutoConfiguration.class})
@EnableJpaRepositories
public @interface EnableSmartJpa {
    
    /**
     * 指定JPA仓库的基础包路径
     * 
     * @return 基础包路径数组
     */
    String[] basePackages() default {};
    
    /**
     * 指定JPA仓库的基础包类
     * 
     * @return 基础包类数组
     */
    Class<?>[] basePackageClasses() default {};
    
    /**
     * 是否启用默认事务
     * 
     * @return 是否启用默认事务
     */
    boolean enableDefaultTransactions() default true;
} 