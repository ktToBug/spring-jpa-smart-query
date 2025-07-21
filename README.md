# Spring JPA Smart Query

🌟 提供基于 JPA 的智能链式查询能力，兼容 Spring Data JPA，支持 lambda 表达式构建条件，类似 MyBatis-Plus 风格但保持 JPA ORM 特性。

---

## ✨ 功能特性

- ✅ 支持 `LambdaQueryWrapper<T>` 链式构建查询条件
- ✅ 支持 lambda 表达式指定字段，类型安全
- ✅ 支持完整的 SQL 操作符（比较、模糊、集合、范围等）
- ✅ 支持排序、分组、HAVING 条件
- ✅ 支持逻辑操作符（AND、OR）
- ✅ 支持嵌套查询和自定义条件
- ✅ 支持分页查询和计数查询
- ✅ 完全兼容 Spring Data JPA，无侵入
- ✅ 一行注解 `@EnableSmartJpa` 即可启用

---

## 📦 快速开始

### 1️⃣ 引入依赖

```xml
<dependency>
    <groupId>io.github.kttobug</groupId>
    <artifactId>spring-jpa-smart-query</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2️⃣ 启用功能

```java
@SpringBootApplication
@EnableSmartJpa // 一键启用所有智能查询功能
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3️⃣ 创建实体类

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Integer age;
    private String email;
    private String department;
    
    // getter/setter 方法...
}
```

### 4️⃣ 扩展仓库接口

```java
public interface UserRepository extends JpaRepository<User, Long>, LambdaQueryExecutor<User> {
    // 继承 JpaRepository 的所有方法
    // 继承 LambdaQueryExecutor 的智能查询方法
}
```

### 5️⃣ 使用智能查询

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findActiveUsers() {
        LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
            .eq(User::getStatus, "ACTIVE")
            .gt(User::getAge, 18)
            .orderByDesc(User::getCreateTime);
            
        return userRepository.list(wrapper);
    }
    
    public Page<User> findUsersByDepartment(String department, Pageable pageable) {
        LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
            .eq(User::getDepartment, department)
            .like(User::getName, "张");
            
        return userRepository.findAll(wrapper, pageable);
    }
}
```

## 🔧 使用示例

### 基础查询：链式调用

```java
// 简单条件查询
LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getName, "张三")
    .gt(User::getAge, 18)
    .like(User::getEmail, "@gmail.com")
    .orderByDesc(User::getCreateTime);

List<User> users = userRepository.list(wrapper);
```

### 复杂查询：多条件组合

```java
// 复杂条件查询
LambdaQueryWrapper<User> complexWrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getStatus, "ACTIVE")
    .in(User::getDepartment, Arrays.asList("IT", "HR"))
    .between(User::getSalary, Arrays.asList(5000, 10000))
    .isNotNull(User::getPhone, Collections.emptyList())
    .groupBy(User::getDepartment)
    .orderByDesc(User::getCreateTime)
    .orderByAsc(User::getName);

List<User> users = userRepository.list(complexWrapper);
```

### 分页和计数查询

```java
// 分页查询
List<User> users = userRepository.listWithPagination(wrapper, 0, 10);

// 计数查询
long count = userRepository.count(wrapper);

// 单条查询
User user = userRepository.getOne(wrapper);

// 存在性检查
boolean exists = userRepository.exists(wrapper);
```

### 逻辑操作符

```java
// OR 条件
LambdaQueryWrapper<User> orWrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getStatus, "ACTIVE")
    .or()
    .eq(User::getRole, "ADMIN");

// 嵌套查询
LambdaQueryWrapper<User> nestedWrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getAge, 30);

LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getDepartment, "IT")
    .nest(nestedWrapper);
```

### 自定义条件

```java
// 自定义 SQL 条件
LambdaQueryWrapper<User> customWrapper = LambdaQueryWrapper.of(User.class)
    .apply("DATE(create_time) = ?", "2024-01-01")
    .apply("LENGTH(name) > ?", 5);
```

---

## 📋 支持的操作符

### 比较操作符
- `eq()` - 等于 (=)
- `ne()` - 不等于 (!=)
- `gt()` - 大于 (>)
- `ge()` - 大于等于 (>=)
- `lt()` - 小于 (<)
- `le()` - 小于等于 (<=)

### 模糊查询
- `like()` - 模糊查询 (%value%)
- `leftLike()` - 左模糊 (%value)
- `rightLike()` - 右模糊 (value%)

### 集合操作
- `in()` - IN 查询
- `notIn()` - NOT IN 查询

### 空值检查
- `isNull()` - IS NULL
- `isNotNull()` - IS NOT NULL

### 范围查询
- `between()` - BETWEEN
- `notBetween()` - NOT BETWEEN

### 排序和分组
- `orderByAsc()` - 升序排序
- `orderByDesc()` - 降序排序
- `groupBy()` - 分组
- `having()` - HAVING 条件

### 逻辑操作
- `or()` - OR 逻辑
- `and()` - AND 逻辑

### 高级操作
- `nest()` - 嵌套查询
- `apply()` - 自定义条件

---

## 🎯 适用场景

| 场景 | 使用方式 | 示例 |
|------|----------|------|
| 快速分页、查询少量字段 | `lambdaQuery().select().eq().in().page()` | 用户列表分页查询 |
| 高度动态拼接条件 | `LambdaQueryWrapper.of(...).eq().in()` | 动态搜索条件 |
| 复杂业务查询 | 组合多种操作符 | 报表数据查询 |
| 与原生 JPA 兼容 | 完全兼容 `JpaRepository` 接口 | 渐进式迁移 |

---

## 🏗️ 项目结构

```
spring-jpa-smart-query/
├── smart-query-core/          # 核心查询构建功能
│   ├── LambdaQueryWrapper     # 查询包装器
│   ├── QueryCondition         # 查询条件
│   ├── QueryOperator          # 操作符枚举
│   └── SerializableFunction   # 序列化函数接口
├── smart-query-spring/        # Spring 集成模块
│   ├── LambdaQueryExecutor    # 查询执行器接口
│   ├── LambdaQueryExecutorImpl # 查询执行器实现
│   └── LambdaQueryAutoConfiguration # 自动配置
├── spring-jpa-smart-query/    # Spring Boot Starter 模块
│   ├── 自动配置和依赖管理
│   └── 配置属性支持
└── smart-query-example/       # 使用示例
```

---

## 🔧 开发环境

- **Java**: 17+
- **Spring Boot**: 3.2.0+
- **Spring Data JPA**: 3.5.1+
- **Maven**: 3.6+

---

## 📝 开发规范

项目遵循以下代码规范：

- **行长度**: 最大 120 字符
- **方法长度**: 最大 150 行
- **参数数量**: 最大 7 个
- **文件长度**: 最大 2000 行
- **编码**: UTF-8
- **JavaDoc**: 所有公共方法必须有完整注释

---

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行代码质量检查
mvn checkstyle:check

# 生成测试报告
mvn surefire-report:report
```

---

## 📦 发布

```bash
# 构建项目
mvn clean package

# 发布到本地仓库
mvn install

# 发布到 Maven Central（需要配置）
mvn deploy -Prelease
```

> 📖 **完整发布指南**：查看 [Maven 发布指南](docs/MAVEN_PUBLISH_GUIDE.md)
> 📖 **API 文档**：查看 [API 文档](docs/API.md)

---

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

## 🧑‍💻 作者

- **开发者**: @kttobug
- **仓库地址**: [https://github.com/kttobug/spring-jpa-smart-query](https://github.com/kttobug/spring-jpa-smart-query)

欢迎 Star、Fork 和 Issue！

---

## 📈 版本历史

### v1.0.0 (2024-01-01)
- ✨ 初始版本发布
- ✨ 支持基础查询操作符
- ✨ 支持排序和分组
- ✨ 支持分页和计数查询
- ✨ 支持逻辑操作符
- ✨ 支持嵌套查询和自定义条件
