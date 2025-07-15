# Maven Central 发布指南

本指南将帮助您将 Spring JPA Smart Query 发布到 Maven Central，供其他开发者使用。

### 构建 jar → 用 GPG 签名 → 配置上传目标（Sonatype） → 上传 → 手动发布
> 注册 Sonatype 账号
> 本地 GPG 生成密钥对
> GitHub 添加 GPG 公钥（可选，但推荐）
> 在 settings.xml 和 pom.xml 中配置 GPG 与 Sonatype 信息
> 执行命令（或脚本）发布到 Maven Central
> 
> mvn clean deploy -s ~/.m2/settings.xml \
-P release \
-Dgpg.passphrase=yybdkt37526SWY

## 📋 准备工作

### 1. 注册 Sonatype 账号

1. 访问 [Sonatype Central Portal](https://central.sonatype.com/register)
2. 注册账号并验证邮箱
3. 创建命名空间（Namespace）申请：
   - 登录后访问 [Central Portal](https://central.sonatype.com/)
   - 点击"Add Namespace"
   - 输入您的命名空间：`io.github.kttobug`
   - 选择 GitHub 验证方式
   - 在您的 GitHub 仓库中创建一个临时的 public repository，名称为验证令牌
   - 等待审核通过（通常1-2个工作日）

### 2. 配置 GPG 密钥

GPG 密钥用于对发布的构件进行数字签名：

```bash
# 安装 GPG（如果尚未安装）
# macOS: brew install gnupg
# Ubuntu: sudo apt-get install gnupg2
# Windows: 下载 Gpg4win

# 生成 GPG 密钥对
gpg --full-generate-key

# 选择密钥类型：RSA and RSA (default)
# 密钥大小：4096
# 有效期：0 (不过期)
# 输入您的姓名和邮箱
# 设置密码（请记住这个密码）

# 查看生成的密钥
gpg --list-keys

# 导出公钥到密钥服务器
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
```

### 3. 配置 Maven settings.xml

在 `~/.m2/settings.xml` 中添加服务器配置：

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_SONATYPE_USERNAME</username>
      <password>YOUR_SONATYPE_PASSWORD</password>
    </server>
  </servers>
  
  <profiles>
    <profile>
      <id>release</id>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>YOUR_GPG_PASSPHRASE</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

## 🚀 发布流程

### 1. 发布前检查

运行以下命令确保项目状态良好：

```bash
# 清理项目
mvn clean

# 运行所有测试
mvn test

# 检查代码质量
mvn checkstyle:check

# 生成 Javadoc
mvn javadoc:javadoc

# 打包项目（包括源码和 Javadoc）
mvn package
```

### 2. 生成所有必需的构件

发布到 Maven Central 需要以下构件：

```bash
# 生成所有必需的构件
mvn clean package -P release

# 这将生成：
# - JAR 文件
# - 源码 JAR (-sources.jar)
# - Javadoc JAR (-javadoc.jar)
# - POM 文件
# - GPG 签名文件 (.asc)
```

### 3. 执行发布

#### 方式一：使用 Central Publishing Plugin（推荐）

```bash
# 发布到 Central（自动签名和发布）
mvn clean deploy -P release
```

#### 方式二：传统方式（如果上述方式失败）

```bash
# 发布到 staging 仓库
mvn clean deploy -P release -Dgpg.passphrase=YOUR_GPG_PASSPHRASE

# 然后在 Sonatype Central Portal 中手动发布
```

### 4. 验证发布内容

发布前，检查 `target` 目录中的构件：

```bash
# 查看 core 模块的构件
ls -la smart-query-core/target/
# 应该包含：
# - smart-query-core-1.0.0.jar
# - smart-query-core-1.0.0-sources.jar
# - smart-query-core-1.0.0-javadoc.jar
# - smart-query-core-1.0.0.pom
# - 以及对应的 .asc 签名文件

# 查看 spring 模块的构件
ls -la smart-query-spring/target/
# 应该包含：
# - smart-query-spring-1.0.0.jar
# - smart-query-spring-1.0.0-sources.jar
# - smart-query-spring-1.0.0-javadoc.jar
# - smart-query-spring-1.0.0.pom
# - 以及对应的 .asc 签名文件
```

## 📦 发布后验证

### 1. 检查发布状态

- 登录 [Central Portal](https://central.sonatype.com/)
- 查看"Deployments"页面
- 确认发布状态为"Published"

### 2. 验证 Maven Central

发布成功后，大约 10-30 分钟后可以在以下地址找到您的构件：

- [Maven Central Search](https://search.maven.org/)
- [MVN Repository](https://mvnrepository.com/)

搜索：`io.github.kttobug`

### 3. 测试依赖

创建一个新的 Maven 项目，添加您的依赖：

```xml
<dependencies>
    <!-- 核心模块 -->
    <dependency>
        <groupId>io.github.kttobug</groupId>
        <artifactId>smart-query-core</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    <!-- Spring 集成模块 -->
    <dependency>
        <groupId>io.github.kttobug</groupId>
        <artifactId>smart-query-spring</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## 🔧 常见问题和解决方案

### 1. GPG 签名失败

```bash
# 检查 GPG 密钥是否存在
gpg --list-secret-keys

# 如果密钥不存在，重新生成
gpg --full-generate-key

# 确保 GPG 密码正确
gpg --armor --detach-sign --batch --yes --passphrase YOUR_PASSPHRASE test.txt
```

### 2. 构件验证失败

确保以下文件都存在：
- JAR 文件
- 源码 JAR
- Javadoc JAR
- POM 文件
- 所有文件的 GPG 签名

### 3. 依赖问题

如果某些依赖无法解析，请检查：
- 依赖的版本是否正确
- 是否有循环依赖
- 是否缺少必要的仓库配置

## 🔄 版本管理

### 语义化版本控制

遵循 [语义化版本](https://semver.org/) 规则：

- **主版本号**：不兼容的 API 修改
- **次版本号**：向后兼容的功能增加
- **修订号**：向后兼容的问题修复

### 发布新版本

```bash
# 1. 更新版本号
mvn versions:set -DnewVersion=1.0.1

# 2. 确认版本更新
mvn versions:commit

# 3. 提交更改
git add .
git commit -m "Release version 1.0.1"
git tag -a v1.0.1 -m "Release version 1.0.1"

# 4. 推送到 GitHub
git push origin main
git push origin v1.0.1

# 5. 发布到 Maven Central
mvn clean deploy -P release
```

## 📝 发布检查清单

在发布前，请确保：

- [ ] 所有测试都通过
- [ ] 代码质量检查通过
- [ ] 版本号已更新
- [ ] GPG 密钥已配置
- [ ] Maven settings.xml 已配置
- [ ] 项目信息完整（名称、描述、许可证等）
- [ ] 源码和 Javadoc 能够正常生成
- [ ] 依赖关系正确
- [ ] Git 标签已创建

## 🎯 自动化发布脚本

以下脚本可以自动化发布过程：

```bash
#!/bin/bash
# release.sh - 自动发布脚本

set -e

# 检查版本号参数
if [ $# -eq 0 ]; then
    echo "Usage: $0 <version>"
    echo "Example: $0 1.0.1"
    exit 1
fi

VERSION=$1

echo "🚀 开始发布版本 $VERSION"

# 1. 清理并测试
echo "📋 清理并运行测试..."
mvn clean test

# 2. 更新版本号
echo "🔄 更新版本号到 $VERSION..."
mvn versions:set -DnewVersion=$VERSION
mvn versions:commit

# 3. 提交更改
echo "📝 提交版本更改..."
git add .
git commit -m "Release version $VERSION"
git tag -a v$VERSION -m "Release version $VERSION"

# 4. 发布到 Maven Central
echo "📦 发布到 Maven Central..."
mvn clean deploy -P release

# 5. 推送到 GitHub
echo "🔄 推送到 GitHub..."
git push origin main
git push origin v$VERSION

echo "✅ 发布完成！"
echo "🔗 查看发布状态：https://central.sonatype.com/"
```

使用方法：
```bash
chmod +x release.sh
./release.sh 1.0.1
``` 