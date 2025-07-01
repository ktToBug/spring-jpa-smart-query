

# Spring JPA Smart Query

🌟 提供基于 JPA 的智能链式查询能力，兼容 Spring Data JPA，支持 lambda 表达式构建条件，类似 MyBatis-Plus 风格但保持 JPA ORM 特性。

---

## ✨ 功能特性

- ✅ 支持 `lambdaQuery()` 链式构建查询条件
- ✅ 支持 `select()` 指定返回字段
- ✅ 支持 `LambdaSpecification<T>` 实现复杂动态条件拼接
- ✅ 完全兼容 Spring Data JPA，无侵入
- ✅ 一行注解 `@EnableSmartJpa` 即可启用

---

## 📦 快速开始

### 1️⃣ 引入依赖

```xml
<dependency>
  <groupId>com.xxx</groupId>
  <artifactId>smart-query-spring</artifactId>
  <version>1.0.0</version>
</dependency>
````

---

### 2️⃣ 启用功能

```java
@SpringBootApplication
@EnableSmartJpa // 包含 @EnableJpaRepositories + 自定义配置
public class Application { }
```

---

## 🔧 简单查询：链式调用

```java
List<User> users = userRepository.lambdaQuery()
    .select(User::getId, User::getUsername)
    .eq(User::getStatus, 1)
    .in(User::getId, List.of(1L, 2L))
    .list();
```

---

## 🧩 复杂查询：LambdaSpecification 拼装

```java
LambdaSpecification<User> spec = LambdaSpecification.of(User.class)
    .eq(User::getStatus, 1);

if (!idList.isEmpty()) {
    spec.in(User::getId, idList);
}

List<User> users = userRepository.findAll(spec);
```

---

## 🎯 适用场景

| 场景           | 使用方式                                      |
| ------------ | ----------------------------------------- |
| 快速分页、查询少量字段  | `lambdaQuery().select().eq().in().page()` |
| 高度动态拼接条件     | `LambdaSpecification.of(...).eq().in()`   |
| 查询 DTO 或 Map | `select(...).list(UserDto.class)` 即将支持    |
| 与原生 JPA 兼容   | 完全兼容 `JpaRepository` 接口                   |

---

## 📌 未来计划

* [x] 支持 `select(...).list(UserDto.class)` 投影
* [x] 支持分页、排序
* [ ] 支持 join、子查询（计划中）
* [ ] 支持 DSL 脚本化构造器（计划中）

---

## 📃 示例仓库

👉 示例代码见 `smart-query-example/` 目录

---

## 🧑‍💻 作者

* 开发者：@kttobug
* 仓库地址：[https://github.com/kttobug/smart-query-spring](https://github.com/kttobug/smart-query-spring)

欢迎 Star、Fork 和 Issue！

```

---

如果你需要：

- 上述 `lambdaQuery().select().eq().list()` 的原型代码，我可以帮你写；
- `LambdaSpecification` 的抽象基类代码，也可以给；
- 封装成 starter + 自动注册 `JpaRepositoryFactoryBean`，也能给思路。

你可以逐个模块实现、逐步完善。

等你确认后我们可以继续写实现或测试用例。
```
