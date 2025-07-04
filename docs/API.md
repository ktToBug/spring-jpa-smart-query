# Spring JPA Smart Query API 文档

## 概述

Spring JPA Smart Query 是一个为 Spring Data JPA 提供智能查询功能的库，支持 lambda 表达式构建类型安全的查询条件。

## 核心类

### LambdaQueryWrapper<T>

查询包装器类，提供链式调用 API 构建查询条件。

#### 构造函数

```java
// 创建查询包装器
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>(User.class);

// 静态工厂方法
LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class);
```

#### 比较操作符

```java
// 等于
wrapper.eq(User::getName, "张三");

// 不等于
wrapper.ne(User::getAge, 25);

// 大于
wrapper.gt(User::getAge, 18);

// 大于等于
wrapper.ge(User::getAge, Arrays.asList(18));

// 小于
wrapper.lt(User::getAge, 65);

// 小于等于
wrapper.le(User::getAge, Arrays.asList(65));
```

#### 模糊查询

```java
// 模糊查询 (%value%)
wrapper.like(User::getName, "张");

// 左模糊查询 (%value)
wrapper.leftLike(User::getName, Arrays.asList("三"));

// 右模糊查询 (value%)
wrapper.rightLike(User::getName, Arrays.asList("张"));
```

#### 集合操作

```java
// IN 查询
wrapper.in(User::getDepartment, Arrays.asList("IT", "HR"));

// NOT IN 查询
wrapper.notIn(User::getDepartment, Arrays.asList("SALES"));
```

#### 空值检查

```java
// IS NULL
wrapper.isNull(User::getPhone, Collections.emptyList());

// IS NOT NULL
wrapper.isNotNull(User::getEmail, Collections.emptyList());
```

#### 范围查询

```java
// BETWEEN
wrapper.between(User::getAge, Arrays.asList(18, 65));

// NOT BETWEEN
wrapper.notBetween(User::getSalary, Arrays.asList(5000, 10000));
```

#### 排序

```java
// 升序排序
wrapper.orderByAsc(User::getName);

// 降序排序
wrapper.orderByDesc(User::getCreateTime);
```

#### 分组

```java
// 单字段分组
wrapper.groupBy(User::getDepartment);

// 多字段分组
wrapper.groupBy(User::getDepartment, User::getRole);
```

#### HAVING 条件

```java
// HAVING 条件
wrapper.having(User::getSalary, QueryOperator.GT, 5000);
```

#### 逻辑操作符

```java
// OR 条件
wrapper.eq(User::getStatus, "ACTIVE")
       .or()
       .eq(User::getRole, "ADMIN");

// AND 条件（默认行为）
wrapper.eq(User::getDepartment, "IT")
       .and()
       .gt(User::getAge, 25);
```

#### JOIN 查询

```java
// 内连接
wrapper.innerJoin(Department.class, "dept", "u.department_id = dept.id");

// 左连接
wrapper.leftJoin(Department.class, "dept", "u.department_id = dept.id");

// 右连接
wrapper.rightJoin(Department.class, "dept", "u.department_id = dept.id");

// 全连接
wrapper.fullJoin(Department.class, "dept", "u.department_id = dept.id");
```

#### 嵌套查询

```java
// 嵌套查询
LambdaQueryWrapper<User> nestedWrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getAge, 30);

wrapper.eq(User::getDepartment, "IT")
       .nest(nestedWrapper);
```

#### 自定义条件

```java
// 自定义 SQL 条件
wrapper.apply("DATE(create_time) = ?", "2024-01-01")
       .apply("LENGTH(name) > ?", 5);
```

#### 工具方法

```java
// 添加条件
QueryCondition condition = new QueryCondition("name", QueryOperator.EQ, "张三");
wrapper.addCondition(condition);

// 清空所有条件
wrapper.clear();

// 获取条件数量
int size = wrapper.size();

// 检查是否为空
boolean isEmpty = wrapper.isEmpty();

// 获取所有条件
List<QueryCondition> conditions = wrapper.getConditions();

// 获取实体类
Class<User> entityClass = wrapper.getEntityClass();
```

### QueryCondition

查询条件类，封装单个查询条件的信息。

#### 构造函数

```java
QueryCondition condition = new QueryCondition("name", QueryOperator.EQ, "张三");
```

#### 属性

- `field`: 字段名
- `operator`: 操作符
- `value`: 条件值

### QueryOperator

查询操作符枚举，定义所有支持的查询操作类型。

#### 操作符列表

- `EQ`: 等于 (=)
- `NE`: 不等于 (!=)
- `GT`: 大于 (>)
- `GE`: 大于等于 (>=)
- `LT`: 小于 (<)
- `LE`: 小于等于 (<=)
- `LIKE`: 模糊查询 (%value%)
- `LEFT_LIKE`: 左模糊查询 (%value)
- `RIGHT_LIKE`: 右模糊查询 (value%)
- `IN`: IN 查询
- `NOT_IN`: NOT IN 查询
- `IS_NULL`: IS NULL
- `IS_NOT_NULL`: IS NOT NULL
- `BETWEEN`: BETWEEN 查询
- `NOT_BETWEEN`: NOT BETWEEN 查询
- `ORDER_BY_ASC`: 升序排序
- `ORDER_BY_DESC`: 降序排序
- `GROUP_BY`: 分组
- `HAVING`: HAVING 条件
- `OR`: OR 逻辑
- `AND`: AND 逻辑
- `NEST`: 嵌套查询
- `APPLY`: 自定义条件

### JoinCondition

JOIN 查询条件类，用于定义表连接关系。

#### 连接类型

- `INNER`: 内连接
- `LEFT`: 左连接
- `RIGHT`: 右连接
- `FULL`: 全连接

#### 静态工厂方法

```java
// 内连接
JoinCondition innerJoin = JoinCondition.innerJoin(Department.class, "dept", "u.department_id = dept.id");

// 左连接
JoinCondition leftJoin = JoinCondition.leftJoin(Department.class, "dept", "u.department_id = dept.id");

// 右连接
JoinCondition rightJoin = JoinCondition.rightJoin(Department.class, "dept", "u.department_id = dept.id");

// 全连接
JoinCondition fullJoin = JoinCondition.fullJoin(Department.class, "dept", "u.department_id = dept.id");
```

### QueryPerformanceMonitor

查询性能监控器，用于监控查询执行时间和统计信息。

#### 主要方法

```java
// 开始监控
QueryPerformanceMonitor.QueryContext context = QueryPerformanceMonitor.startMonitoring("user_query");

// 结束监控
QueryPerformanceMonitor.endMonitoring(context);

// 获取统计信息
QueryPerformanceMonitor.QueryStats stats = QueryPerformanceMonitor.getStats("user_query");

// 获取总体统计
QueryPerformanceMonitor.QueryStats overallStats = QueryPerformanceMonitor.getOverallStats();

// 重置统计
QueryPerformanceMonitor.resetStats();
```

## Spring 集成

### LambdaQueryExecutor<T>

查询执行器接口，提供查询执行方法。

#### 主要方法

```java
// 查询列表
List<T> list(LambdaQueryWrapper<T> queryWrapper);

// 分页查询
List<T> listWithPagination(LambdaQueryWrapper<T> queryWrapper, int page, int size);

// 计数查询
long count(LambdaQueryWrapper<T> queryWrapper);

// 单条查询
T getOne(LambdaQueryWrapper<T> queryWrapper);

// 存在性检查
boolean exists(LambdaQueryWrapper<T> queryWrapper);
```

### LambdaQueryExecutorImpl<T>

查询执行器实现类，基于 JPA Criteria API 实现查询功能。

## 最佳实践

### 1. 查询条件构建

```java
// 推荐：链式调用，清晰易读
LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getStatus, "ACTIVE")
    .gt(User::getAge, 18)
    .like(User::getEmail, "@gmail.com")
    .orderByDesc(User::getCreateTime);

// 不推荐：硬编码字段名
// wrapper.eq("name", "张三"); // 容易出错，没有类型检查
```

### 2. 动态条件构建

```java
LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class);

// 根据条件动态添加
if (StringUtils.hasText(name)) {
    wrapper.like(User::getName, name);
}

if (age != null) {
    wrapper.gt(User::getAge, age);
}

if (departments != null && !departments.isEmpty()) {
    wrapper.in(User::getDepartment, departments);
}
```

### 3. 复杂查询

```java
// 复杂查询示例
LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getStatus, "ACTIVE")
    .in(User::getDepartment, Arrays.asList("IT", "HR"))
    .between(User::getSalary, Arrays.asList(5000, 10000))
    .isNotNull(User::getPhone, Collections.emptyList())
    .groupBy(User::getDepartment)
    .having(User::getSalary, QueryOperator.GT, 5000)
    .orderByDesc(User::getCreateTime)
    .orderByAsc(User::getName);
```

### 4. JOIN 查询

```java
// JOIN 查询示例
LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getStatus, "ACTIVE")
    .leftJoin(Department.class, "dept", "u.department_id = dept.id")
    .eq(User::getAge, 25)
    .orderByDesc(User::getCreateTime);
```

### 5. 性能监控

```java
// 在查询执行前后添加性能监控
QueryPerformanceMonitor.QueryContext context = QueryPerformanceMonitor.startMonitoring("user_list_query");

try {
    List<User> users = userRepository.list(wrapper);
    return users;
} finally {
    QueryPerformanceMonitor.endMonitoring(context);
}
```

### 6. 错误处理

```java
try {
    LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
        .eq(User::getName, "张三");
    
    List<User> users = userRepository.list(wrapper);
    return users;
} catch (Exception e) {
    // 记录错误日志
    log.error("查询用户失败", e);
    throw new QueryException("查询用户失败", e);
}
```

## 注意事项

1. **类型安全**: 始终使用 lambda 表达式指定字段，避免硬编码字段名
2. **空值处理**: 查询条件会自动忽略 null 值，无需手动检查
3. **性能考虑**: 对于复杂查询，考虑使用性能监控功能
4. **事务管理**: 确保在正确的事务上下文中执行查询
5. **索引优化**: 根据查询条件为相关字段创建数据库索引

## 版本兼容性

- Java: 17+
- Spring Boot: 3.2.0+
- Spring Data JPA: 3.5.1+
- JPA Provider: Hibernate 6.x, EclipseLink 等 