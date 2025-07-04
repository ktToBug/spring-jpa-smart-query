package io.github.kttobug.spring;


import io.github.kttobug.query.LambdaQueryWrapper;

import java.util.List;

public interface LambdaQueryExecutor<T> {
    List<T> list(LambdaQueryWrapper<T> queryWrapper);

    default List<T> findAll(LambdaQueryWrapper<T> queryWrapper) {
        return list(queryWrapper);
    }

    // 分页查询
    default List<T> listWithPagination(LambdaQueryWrapper<T> queryWrapper, int page, int size) {
        return list(queryWrapper);
    }

    // 计数查询
    default long count(LambdaQueryWrapper<T> queryWrapper) {
        return 0L;
    }

    // 获取单个结果
    default T getOne(LambdaQueryWrapper<T> queryWrapper) {
        List<T> results = list(queryWrapper);
        return results.isEmpty() ? null : results.get(0);
    }

    // 检查是否存在
    default boolean exists(LambdaQueryWrapper<T> queryWrapper) {
        return count(queryWrapper) > 0;
    }
}