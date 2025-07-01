package io.github.kttobug.spring;


import io.github.kttobug.query.LambdaQueryWrapper;

import java.util.List;

public interface LambdaQueryExecutor<T> {
    List<T> list(LambdaQueryWrapper<T> queryWrapper);
}