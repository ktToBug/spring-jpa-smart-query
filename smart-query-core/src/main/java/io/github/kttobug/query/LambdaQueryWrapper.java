package io.github.kttobug.query;

import io.github.kttobug.query.util.LambdaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Lambda 表达式查询包装器，提供类型安全的查询条件构建功能。
 * 
 * <p>该类支持链式调用，可以构建复杂的查询条件，包括比较操作、模糊查询、
 * 集合操作、排序、分组等功能。所有查询条件都通过 lambda 表达式指定字段，
 * 避免了硬编码字段名，提供了编译时类型检查。</p>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
 *     .eq(User::getName, "张三")
 *     .gt(User::getAge, 18)
 *     .like(User::getEmail, "@gmail.com")
 *     .orderByDesc(User::getCreateTime);
 * }</pre>
 * 
 * @param <T> 实体类型
 * @author kttobug
 * @since 1.0.0
 */
public class LambdaQueryWrapper<T> {

    /** 查询条件列表 */
    private final List<QueryCondition> conditions = new ArrayList<>();
    
    /** 实体类 */
    private final Class<T> entityClass;
    
    /** JOIN 条件列表 */
    private final List<JoinCondition> joinConditions = new ArrayList<>();

    /**
     * 构造函数
     * 
     * @param entityClass 实体类
     */
    public LambdaQueryWrapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * 创建查询包装器的静态工厂方法
     * 
     * @param clazz 实体类
     * @param <T> 实体类型
     * @return 查询包装器实例
     */
    public static <T> LambdaQueryWrapper<T> of(Class<T> clazz) {
        return new LambdaQueryWrapper<>(clazz);
    }

    /**
     * 等于条件
     * 
     * <p>构建字段等于指定值的查询条件。如果值为 null，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param value 比较值
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> eq(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.EQ, value));
        }
        return this;
    }

    /**
     * 模糊查询条件
     * 
     * <p>构建字段包含指定值的模糊查询条件（%value%）。如果值为 null，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param value 模糊匹配值
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> like(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LIKE, value));
        }
        return this;
    }

    /**
     * 不等于条件
     * 
     * <p>构建字段不等于指定值的查询条件。如果值为 null，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param value 比较值
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> ne(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.NE, value));
        }
        return this;
    }

    /**
     * 大于条件
     * 
     * <p>构建字段大于指定值的查询条件。如果值为 null，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param value 比较值
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> gt(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.GT, value));
        }
        return this;
    }

    /**
     * 大于等于条件
     * 
     * <p>构建字段大于等于指定值的查询条件。如果值为 null 或集合为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 比较值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> ge(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.GE, values));
        }
        return this;
    }

    /**
     * 小于条件
     * 
     * <p>构建字段小于指定值的查询条件。如果值为 null，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param value 比较值
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> lt(SerializableFunction<T, R> field, R value) {
        if (value != null) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LT, value));
        }
        return this;
    }

    /**
     * 模糊查询条件（集合版本）
     * 
     * <p>构建字段包含指定值集合中任一值的模糊查询条件。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 模糊匹配值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> like(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LIKE, values));
        }
        return this;
    }

    /**
     * 左模糊查询条件
     * 
     * <p>构建字段以指定值结尾的模糊查询条件（%value）。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 模糊匹配值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> leftLike(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LEFT_LIKE, values));
        }
        return this;
    }

    /**
     * 右模糊查询条件
     * 
     * <p>构建字段以指定值开头的模糊查询条件（value%）。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 模糊匹配值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> rightLike(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.RIGHT_LIKE, values));
        }
        return this;
    }

    /**
     * 小于等于条件
     * 
     * <p>构建字段小于等于指定值的查询条件。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 比较值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> le(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.LE, values));
        }
        return this;
    }

    /**
     * IN 查询条件
     * 
     * <p>构建字段在指定值集合中的查询条件。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> in(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.IN, values));
        }
        return this;
    }

    /**
     * NOT IN 查询条件
     * 
     * <p>构建字段不在指定值集合中的查询条件。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 值集合
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> notIn(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.NOT_IN, values));
        }
        return this;
    }

    /**
     * IS NULL 条件
     * 
     * <p>构建字段为 NULL 的查询条件。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 值集合（此参数在此方法中不使用，但保持接口一致性）
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> isNull(SerializableFunction<T, R> field, Iterable<R> values) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.IS_NULL, values));
        return this;
    }

    /**
     * IS NOT NULL 条件
     * 
     * <p>构建字段不为 NULL 的查询条件。如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 值集合（此参数在此方法中不使用，但保持接口一致性）
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> isNotNull(SerializableFunction<T, R> field, Iterable<R> values) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.IS_NOT_NULL, values));
        return this;
    }

    /**
     * BETWEEN 范围查询条件
     * 
     * <p>构建字段在指定范围内的查询条件。集合中需要包含两个值（起始值和结束值）。
     * 如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 范围值集合（需要包含起始值和结束值）
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> between(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.BETWEEN, values));
        }
        return this;
    }

    /**
     * NOT BETWEEN 范围查询条件
     * 
     * <p>构建字段不在指定范围内的查询条件。集合中需要包含两个值（起始值和结束值）。
     * 如果集合为 null 或为空，则忽略此条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param values 范围值集合（需要包含起始值和结束值）
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> notBetween(SerializableFunction<T, R> field, Iterable<R> values) {
        if (values != null && values.iterator().hasNext()) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.NOT_BETWEEN, values));
        }
        return this;
    }

    /**
     * 升序排序
     * 
     * <p>添加字段的升序排序条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> orderByAsc(SerializableFunction<T, R> field) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.ORDER_BY_ASC, null));
        return this;
    }

    /**
     * 降序排序
     * 
     * <p>添加字段的降序排序条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> orderByDesc(SerializableFunction<T, R> field) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.ORDER_BY_DESC, null));
        return this;
    }

    /**
     * 分组条件
     * 
     * <p>添加字段的分组条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> groupBy(SerializableFunction<T, R> field) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.GROUP_BY, null));
        return this;
    }

    /**
     * 多字段分组条件
     * 
     * <p>添加多个字段的分组条件。</p>
     * 
     * @param fields 字段的 lambda 表达式数组
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> groupBy(SerializableFunction<T, R>... fields) {
        for (SerializableFunction<T, R> field : fields) {
            String fieldName = LambdaUtils.resolveFieldName(field);
            conditions.add(new QueryCondition(fieldName, QueryOperator.GROUP_BY, null));
        }
        return this;
    }

    /**
     * HAVING 条件
     * 
     * <p>添加分组后的过滤条件。</p>
     * 
     * @param field 字段的 lambda 表达式
     * @param operator 操作符
     * @param value 比较值
     * @param <R> 字段类型
     * @return 当前查询包装器实例，支持链式调用
     */
    public <R> LambdaQueryWrapper<T> having(SerializableFunction<T, R> field, QueryOperator operator, Object value) {
        String fieldName = LambdaUtils.resolveFieldName(field);
        conditions.add(new QueryCondition(fieldName, QueryOperator.HAVING, new Object[]{operator, value}));
        return this;
    }

    /**
     * OR 逻辑操作符
     * 
     * <p>添加 OR 逻辑操作符，用于连接多个条件。</p>
     * 
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> or() {
        conditions.add(new QueryCondition("", QueryOperator.OR, null));
        return this;
    }

    /**
     * AND 逻辑操作符
     * 
     * <p>添加 AND 逻辑操作符，用于连接多个条件。</p>
     * 
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> and() {
        conditions.add(new QueryCondition("", QueryOperator.AND, null));
        return this;
    }

    /**
     * 嵌套查询
     * 
     * <p>添加嵌套查询条件。</p>
     * 
     * @param nestedWrapper 嵌套的查询包装器
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> nest(LambdaQueryWrapper<T> nestedWrapper) {
        conditions.add(new QueryCondition("", QueryOperator.NEST, nestedWrapper));
        return this;
    }

    /**
     * 自定义条件
     * 
     * <p>添加自定义 SQL 条件。</p>
     * 
     * @param customCondition 自定义条件字符串
     * @param parameters 条件参数
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> apply(String customCondition, Object... parameters) {
        Object[] applyData = new Object[parameters.length + 1];
        applyData[0] = customCondition;
        System.arraycopy(parameters, 0, applyData, 1, parameters.length);
        conditions.add(new QueryCondition("", QueryOperator.APPLY, applyData));
        return this;
    }

    /**
     * 添加查询条件
     * 
     * <p>直接添加查询条件对象。</p>
     * 
     * @param condition 查询条件
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> addCondition(QueryCondition condition) {
        if (condition != null) {
            conditions.add(condition);
        }
        return this;
    }

    /**
     * 清空所有条件
     * 
     * <p>移除所有已添加的查询条件。</p>
     * 
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> clear() {
        conditions.clear();
        return this;
    }

    /**
     * 获取条件数量
     * 
     * @return 当前查询条件的数量
     */
    public int size() {
        return conditions.size();
    }

    /**
     * 检查是否为空
     * 
     * @return 如果没有查询条件则返回 true，否则返回 false
     */
    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    /**
     * 获取所有查询条件
     * 
     * @return 查询条件列表
     */
    public List<QueryCondition> getConditions() {
        return conditions;
    }

    /**
     * 获取实体类
     * 
     * @return 实体类
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 内连接
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> innerJoin(Class<?> targetEntity, String alias, String joinCondition) {
        joinConditions.add(JoinCondition.innerJoin(targetEntity, alias, joinCondition));
        return this;
    }

    /**
     * 左连接
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> leftJoin(Class<?> targetEntity, String alias, String joinCondition) {
        joinConditions.add(JoinCondition.leftJoin(targetEntity, alias, joinCondition));
        return this;
    }

    /**
     * 右连接
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> rightJoin(Class<?> targetEntity, String alias, String joinCondition) {
        joinConditions.add(JoinCondition.rightJoin(targetEntity, alias, joinCondition));
        return this;
    }

    /**
     * 全连接
     * 
     * @param targetEntity 目标实体类
     * @param alias 别名
     * @param joinCondition 连接条件
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> fullJoin(Class<?> targetEntity, String alias, String joinCondition) {
        joinConditions.add(JoinCondition.fullJoin(targetEntity, alias, joinCondition));
        return this;
    }

    /**
     * 添加 JOIN 条件
     * 
     * @param joinCondition JOIN 条件
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> addJoin(JoinCondition joinCondition) {
        if (joinCondition != null) {
            joinConditions.add(joinCondition);
        }
        return this;
    }

    /**
     * 获取所有 JOIN 条件
     * 
     * @return JOIN 条件列表
     */
    public List<JoinCondition> getJoinConditions() {
        return joinConditions;
    }

    /**
     * 检查是否有 JOIN 条件
     * 
     * @return 如果有 JOIN 条件则返回 true，否则返回 false
     */
    public boolean hasJoins() {
        return !joinConditions.isEmpty();
    }

    /**
     * 清空所有 JOIN 条件
     * 
     * @return 当前查询包装器实例，支持链式调用
     */
    public LambdaQueryWrapper<T> clearJoins() {
        joinConditions.clear();
        return this;
    }
}
