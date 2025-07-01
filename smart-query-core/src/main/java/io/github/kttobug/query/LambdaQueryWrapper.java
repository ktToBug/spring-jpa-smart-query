package io.github.kttobug.query;

import io.github.kttobug.query.util.LambdaUtils;

import java.util.ArrayList;
import java.util.List;

public class LambdaQueryWrapper<T> {

    private final List<QueryCondition> conditions = new ArrayList<>();
    private final Class<T> entityClass;

    public LambdaQueryWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> LambdaQueryWrapper<T> of(Class<T> clazz) {
        return new LambdaQueryWrapper<>(clazz);
    }

    public <R> LambdaQueryWrapper<T> eq(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.EQ, value));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> like(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LIKE, value));
        }
        return this;
    }

    // 在 LambdaQueryWrapper.java 中添加

    public <R> LambdaQueryWrapper<T> ne(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.NE, value));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> gt(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.GT, value));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> lt(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LT, value));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> in(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.IN, values));
        }
        return this;
    }

    public List<QueryCondition> getConditions() {
        return conditions;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
