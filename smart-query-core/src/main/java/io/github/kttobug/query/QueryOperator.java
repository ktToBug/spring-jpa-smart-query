package io.github.kttobug.query;

/**
 * 查询操作符枚举，定义了所有支持的查询操作类型。
 * 
 * <p>该枚举包含了常见的 SQL 查询操作符，用于构建各种类型的查询条件。
 * 每个操作符都有对应的 SQL 语义，在查询构建过程中会被转换为相应的 JPA Criteria API 调用。</p>
 * 
 * <p>操作符分类：</p>
 * <ul>
 *   <li><strong>比较操作符</strong>: EQ, NE, GT, GE, LT, LE</li>
 *   <li><strong>模糊查询</strong>: LIKE, LEFT_LIKE, RIGHT_LIKE</li>
 *   <li><strong>集合操作</strong>: IN, NOT_IN</li>
 *   <li><strong>空值检查</strong>: IS_NULL, IS_NOT_NULL</li>
 *   <li><strong>范围查询</strong>: BETWEEN, NOT_BETWEEN</li>
 *   <li><strong>排序操作</strong>: ORDER_BY_ASC, ORDER_BY_DESC</li>
 *   <li><strong>分组操作</strong>: GROUP_BY, HAVING</li>
 *   <li><strong>逻辑操作</strong>: OR, AND</li>
 *   <li><strong>高级操作</strong>: NEST, APPLY</li>
 * </ul>
 * 
 * @author kttobug
 * @since 1.0.0
 */
public enum QueryOperator {
    
    /** 等于操作符 (=) */
    EQ,

    /** 不等于操作符 (!=) */
    NE,
    
    /** 大于操作符 (>) */
    GT,
    
    /** 大于等于操作符 (>=) */
    GE,
    
    /** 小于操作符 (<) */
    LT,
    
    /** 小于等于操作符 (<=) */
    LE,

    /** 模糊查询操作符 (LIKE '%value%') */
    LIKE,
    
    /** 右模糊查询操作符 (LIKE 'value%') */
    RIGHT_LIKE,
    
    /** 左模糊查询操作符 (LIKE '%value') */
    LEFT_LIKE,

    /** IN 查询操作符 (IN (value1, value2, ...)) */
    IN,
    
    /** NOT IN 查询操作符 (NOT IN (value1, value2, ...)) */
    NOT_IN,

    /** IS NULL 操作符 (IS NULL) */
    IS_NULL,
    
    /** IS NOT NULL 操作符 (IS NOT NULL) */
    IS_NOT_NULL,

    /** BETWEEN 范围查询操作符 (BETWEEN value1 AND value2) */
    BETWEEN,
    
    /** NOT BETWEEN 范围查询操作符 (NOT BETWEEN value1 AND value2) */
    NOT_BETWEEN,

    /** 升序排序操作符 (ORDER BY field ASC) */
    ORDER_BY_ASC,
    
    /** 降序排序操作符 (ORDER BY field DESC) */
    ORDER_BY_DESC,

    /** 分组操作符 (GROUP BY field) */
    GROUP_BY,
    
    /** HAVING 条件操作符 (HAVING condition) */
    HAVING,

    /** OR 逻辑操作符 (OR) */
    OR,
    
    /** AND 逻辑操作符 (AND) */
    AND,

    /** 嵌套查询操作符 (用于子查询) */
    NEST,
    
    /** 自定义条件操作符 (用于自定义 SQL 片段) */
    APPLY,
}