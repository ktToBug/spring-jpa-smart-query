# 快速入门指南

本指南将帮助您在 5 分钟内快速集成并使用 Spring JPA Smart Query。

## 1. 添加依赖

在您的 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>io.github.kttobug</groupId>
    <artifactId>smart-query-spring</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 2. 启用功能

在您的启动类上添加 `@EnableSmartJpa` 注解：

```java
@SpringBootApplication
@EnableSmartJpa
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 3. 创建实体类

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

## 4. 扩展仓库接口

```java
public interface UserRepository extends JpaRepository<User, Long>, LambdaQueryExecutor<User> {
    // 继承 JpaRepository 的所有方法
    // 继承 LambdaQueryExecutor 的智能查询方法
}
```

## 5. 使用智能查询

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

## 6. 支持的查询操作

### 基本条件查询
```java
// 等于条件
.eq(User::getName, "张三")

// 不等于条件
.ne(User::getAge, 25)

// 大于/小于条件
.gt(User::getAge, 18)
.lt(User::getAge, 65)

// 模糊查询
.like(User::getName, "张")
.leftLike(User::getName, "三")
.rightLike(User::getName, "张")
```

### 集合条件查询
```java
// IN 查询
.in(User::getDepartment, Arrays.asList("IT", "HR"))

// NOT IN 查询
.notIn(User::getStatus, Arrays.asList("DELETED", "BANNED"))

// 范围查询
.between(User::getAge, Arrays.asList(18, 65))
```

### 空值检查
```java
// IS NULL
.isNull(User::getPhone, Collections.emptyList())

// IS NOT NULL
.isNotNull(User::getEmail, Collections.emptyList())
```

### 排序和分组
```java
// 排序
.orderByAsc(User::getName)
.orderByDesc(User::getCreateTime)

// 分组
.groupBy(User::getDepartment)
```

### 逻辑操作
```java
// OR 条件
.eq(User::getStatus, "ACTIVE")
.or()
.eq(User::getRole, "ADMIN")

// 嵌套查询
LambdaQueryWrapper<User> nestedWrapper = LambdaQueryWrapper.of(User.class)
    .eq(User::getAge, 30);
    
wrapper.eq(User::getDepartment, "IT")
       .nest(nestedWrapper);
```

## 7. 高级功能

### 自定义 SQL 条件
```java
.apply("DATE(create_time) = ?", "2024-01-01")
.apply("LENGTH(name) > ?", 5)
```

### 分页查询
```java
// 分页查询
List<User> users = userRepository.listWithPagination(wrapper, 0, 10);

// 计数查询
long count = userRepository.count(wrapper);
```

### JOIN 查询
```java
wrapper.innerJoin(Department.class, "dept", "u.department_id = dept.id")
       .eq(User::getStatus, "ACTIVE");
```

## 8. 性能监控

启用性能监控功能：

```java
@Configuration
public class QueryConfig {
    
    @Bean
    public QueryPerformanceMonitor queryPerformanceMonitor() {
        return new QueryPerformanceMonitor();
    }
}
```

## 9. 完整示例

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        
        LambdaQueryWrapper<User> wrapper = LambdaQueryWrapper.of(User.class);
        
        if (name != null) {
            wrapper.like(User::getName, name);
        }
        
        if (department != null) {
            wrapper.eq(User::getDepartment, department);
        }
        
        if (minAge != null) {
            wrapper.ge(User::getAge, Arrays.asList(minAge));
        }
        
        if (maxAge != null) {
            wrapper.le(User::getAge, Arrays.asList(maxAge));
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        
        List<User> users = userRepository.list(wrapper);
        return ResponseEntity.ok(users);
    }
}
```

## 10. 常见问题

### Q: 如何处理空值？
A: 大部分方法会自动忽略 null 值，但某些方法（如 isNull/isNotNull）会始终添加条件。

### Q: 如何优化查询性能？
A: 使用分页查询、合理的索引、避免不必要的 JOIN 操作。

### Q: 如何调试生成的 SQL？
A: 开启 JPA 的 SQL 日志输出：
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Q: 支持哪些数据库？
A: 支持所有 JPA 兼容的数据库，包括 MySQL、PostgreSQL、Oracle、SQL Server 等。

---

**恭喜！** 您已经成功集成了 Spring JPA Smart Query。现在可以享受类型安全、链式调用的查询体验了！

更多详细信息请参考：
- [API 文档](API.md)
- [完整示例](../smart-query-example/)
- [GitHub 仓库](https://github.com/kttobug/spring-jpa-smart-query) 