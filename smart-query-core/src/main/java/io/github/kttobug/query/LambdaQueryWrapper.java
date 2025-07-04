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

    public <R> LambdaQueryWrapper<T> ge(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.GE, values));
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

    public <R> LambdaQueryWrapper<T> like(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LIKE, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> leftLike(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LEFT_LIKE, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> rightLike(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.RIGHT_LIKE, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> le(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LE, values));
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

    public <R> LambdaQueryWrapper<T> notIn(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.NOT_IN, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> isNull(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.IS_NULL, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> isNotNull(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.IS_NOT_NULL, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> between(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.BETWEEN, values));
        }
        return this;
    }

    public <R> LambdaQueryWrapper<T> notBetween(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.NOT_BETWEEN, values));
        }
        return this;
    }

    // 排序方法
    public <R> LambdaQueryWrapper<T> orderByAsc(SerializableFunction<T, R> field) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.ORDER_BY_ASC, null));
        return this;
    }

    public <R> LambdaQueryWrapper<T> orderByDesc(SerializableFunction<T, R> field) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.ORDER_BY_DESC, null));
        return this;
    }

    // 分组方法
    public <R> LambdaQueryWrapper<T> groupBy(SerializableFunction<T, R> field) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.GROUP_BY, null));
        return this;
    }

    public <R> LambdaQueryWrapper<T> groupBy(SerializableFunction<T, R>... fields) {
        for (SerializableFunction<T, R> field : fields) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.GROUP_BY, null));
        }
        return this;
    }

    // HAVING 条件方法
    public <R> LambdaQueryWrapper<T> having(SerializableFunction<T, R> field, QueryOperator operator, Object value) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.HAVING, new Object[]{operator, value}));
        return this;
    }

    // 逻辑操作符方法
    public LambdaQueryWrapper<T> or() {
        conditions.add(new QueryCondition("", QueryOperator.OR, null));
        return this;
    }

    public LambdaQueryWrapper<T> and() {
        conditions.add(new QueryCondition("", QueryOperator.AND, null));
        return this;
    }

    // 嵌套查询方法
    public LambdaQueryWrapper<T> nest(LambdaQueryWrapper<T> nestedWrapper) {
        conditions.add(new QueryCondition("", QueryOperator.NEST, nestedWrapper));
        return this;
    }

    // 应用自定义条件方法
    public LambdaQueryWrapper<T> apply(String customCondition, Object... parameters) {
        conditions.add(new QueryCondition("", QueryOperator.APPLY, new Object[]{customCondition, parameters}));
        return this;
    }

    // 批量添加条件的方法
    public LambdaQueryWrapper<T> addCondition(QueryCondition condition) {
        if (condition != null) {
            conditions.add(condition);
        }
        return this;
    }

    // 清空所有条件
    public LambdaQueryWrapper<T> clear() {
        conditions.clear();
        return this;
    }

    // 获取条件数量
    public int size() {
        return conditions.size();
    }

    // 检查是否为空
    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public List<QueryCondition> getConditions() {
        return conditions;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }
}
