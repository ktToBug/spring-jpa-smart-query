package io.github.kttobug.spring.aspect;

import io.github.kttobug.query.LambdaQueryWrapper;
import io.github.kttobug.spring.LambdaQueryExecutor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LambdaQueryExecutorAspect {

    @Autowired
    private LambdaQueryExecutor lambdaQueryExecutor;

    @Around("execution(* io.github.kttobug.query.LambdaQueryWrapper.list())")
    public Object aroundListMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        LambdaQueryWrapper<?> wrapper = (LambdaQueryWrapper<?>) joinPoint.getTarget();
        return lambdaQueryExecutor.list(wrapper);
    }
}
