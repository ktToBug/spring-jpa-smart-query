# Maven Central 发布指南

本指南将帮助您将 Spring JPA Smart Query 发布到 Maven Central，供其他开发者使用。

## 📋 准备工作

### 1. 注册 Sonatype 账号

1. 访问 [Sonatype OSSRH](https://central.sonatype.com/register)
2. 注册账号并验证邮箱
3. 创建命名空间（Namespace）申请：
   - 登录后访问 [Central Portal](https://central.sonatype.com/)
   - 点击"Add Namespace"
   - 输入您的命名空间：`io.github.kttobug`
   - 选择验证方式（GitHub验证推荐）
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

### 1. 确认项目配置

确保您的 `pom.xml` 包含以下必要信息：

```xml
<!-- 项目基本信息 -->
<groupId>io.github.kttobug</groupId>
<artifactId>spring-jpa-smart-query</artifactId>
<version>1.0.0</version>
<packaging>pom</packaging>

<name>Spring JPA Smart Query</name>
<description>A smart query library for Spring Data JPA with lambda expression support</description>
<url>https://github.com/kttobug/spring-jpa-smart-query</url>

<!-- 许可证信息 -->
<licenses>
  <license>
    <name>MIT License</name>
    <url>https://opensource.org/licenses/MIT</url>
    <distribution>repo</distribution>
  </license>
</licenses>

<!-- 开发者信息 -->
<developers>
  <developer>
    <name>kttobug</name>
    <email>kttobug@example.com</email>
    <organization>kttobug</organization>
    <organizationUrl>https://github.com/kttobug</organizationUrl>
  </developer>
</developers>

<!-- SCM 信息 -->
<scm>
  <connection>scm:git:git://github.com/kttobug/spring-jpa-smart-query.git</connection>
  <developerConnection>scm:git:ssh://github.com:kttobug/spring-jpa-smart-query.git</developerConnection>
  <url>https://github.com/kttobug/spring-jpa-smart-query/tree/main</url>
</scm>

<!-- 发布仓库配置 -->
<distributionManagement>
  <snapshotRepository>
    <id>central</id>
    <name>Central Repository OSSRH</name>
    <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
  </snapshotRepository>
  <repository>
    <id>central</id>
    <name>Central Repository OSSRH</name>
    <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
  </repository>
</distributionManagement>
```

### 2. 添加发布插件

在 `pom.xml` 的 `<build>` 部分添加：

```xml
<build>
  <plugins>
    <!-- Maven GPG Plugin -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-gpg-plugin</artifactId>
      <version>3.1.0</version>
      <executions>
        <execution>
          <id>sign-artifacts</id>
          <phase>verify</phase>
          <goals>
            <goal>sign</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

    <!-- Central Publishing Plugin -->
    <plugin>
      <groupId>org.sonatype.central</groupId>
      <artifactId>central-publishing-maven-plugin</artifactId>
      <version>0.4.0</version>
      <extensions>true</extensions>
      <configuration>
        <publishingServerId>central</publishingServerId>
        <tokenAuth>true</tokenAuth>
        <autoPublish>true</autoPublish>
      </configuration>
    </plugin>
  </plugins>
</build>
```

### 3. 运行发布前检查

```bash
# 清理项目
mvn clean

# 运行所有测试
mvn test

# 检查代码质量
mvn checkstyle:check

# 生成 Javadoc
mvn javadoc:javadoc

# 打包项目
mvn package
```

### 4. 执行发布

```bash
# 发布到 Central
mvn clean deploy -Prelease

# 或者使用新的 Central Publishing Plugin
mvn clean deploy
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

搜索：`io.github.kttobug:smart-query-spring`

### 3. 测试依赖

创建一个新的 Maven 项目，添加您的依赖：

```xml
<dependency>
    <groupId>io.github.kttobug</groupId>
    <artifactId>smart-query-spring</artifactId>
    <version>1.0.0</version>
</dependency>
```

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

# 2. 提交更改
git add .
git commit -m "Release version 1.0.1"
git tag -a v1.0.1 -m "Release version 1.0.1"

# 3. 推送到 GitHub
git push origin main
git push origin v1.0.1

# 4. 发布到 Maven Central
mvn clean deploy -Prelease
```

## 🤖 自动化发布

### GitHub Actions 配置

创建 `.github/workflows/release.yml`：

```yaml
name: Release to Maven Central

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        server-id: central
        server-username: MAVEN_USERNAME
        server-password: MAVEN_PASSWORD
        gpg-private-key: ${{ secrets.GPG_PRIVATE_KEY }}
        gpg-passphrase: MAVEN_GPG_PASSPHRASE
    
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2
    
    - name: Run tests
      run: mvn clean test
    
    - name: Deploy to Maven Central
      run: mvn clean deploy -Prelease
      env:
        MAVEN_USERNAME: ${{ secrets.OSSRH_USERNAME }}
        MAVEN_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
        MAVEN_GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
```

### GitHub Secrets 配置

在 GitHub 仓库的 Settings → Secrets 中添加：

- `OSSRH_USERNAME`：Sonatype 用户名
- `OSSRH_PASSWORD`：Sonatype 密码
- `GPG_PRIVATE_KEY`：GPG 私钥
- `GPG_PASSPHRASE`：GPG 密码

## 🛠️ 故障排除

### 常见问题

1. **GPG 签名失败**
   ```bash
   # 检查 GPG 配置
   gpg --list-keys
   
   # 重新导出公钥
   gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
   ```

2. **401 未授权错误**
   - 检查 settings.xml 中的用户名密码
   - 确认 Sonatype 账号已激活

3. **命名空间验证失败**
   - 确认 GitHub 仓库所有者与申请的命名空间匹配
   - 检查命名空间审核状态

4. **构件验证失败**
   - 确保所有必需的 metadata 都已包含
   - 检查 POM 文件的完整性

### 检查列表

发布前请确认：

- [ ] 所有测试通过
- [ ] 代码质量检查通过
- [ ] 版本号已更新
- [ ] 文档已更新
- [ ] GPG 密钥配置正确
- [ ] Maven settings.xml 配置正确
- [ ] POM 文件信息完整

## 📚 参考资源

- [Maven Central 官方文档](https://central.sonatype.org/publish/publish-guide/)
- [GPG 签名指南](https://central.sonatype.org/publish/requirements/gpg/)
- [语义化版本控制](https://semver.org/)
- [GitHub Actions 文档](https://docs.github.com/en/actions)

---

**恭喜！** 您已经成功将项目发布到 Maven Central。现在全世界的开发者都可以使用您的库了！ 🎉 