# 发布指南

本文档描述了如何将 Spring JPA Smart Query 发布到 Maven Central。

## 发布前准备

### 1. 注册 Sonatype 账号

1. 访问 [Sonatype OSSRH](https://s01.oss.sonatype.org/)
2. 注册账号并创建工单申请发布权限
3. 等待审核通过

### 2. 配置 GPG 密钥

```bash
# 生成 GPG 密钥对
gpg --gen-key

# 列出密钥
gpg --list-keys

# 导出公钥到 Sonatype
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
```

### 3. 配置 Maven settings.xml

在 `~/.m2/settings.xml` 中添加：

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>YOUR_SONATYPE_USERNAME</username>
      <password>YOUR_SONATYPE_PASSWORD</password>
    </server>
  </servers>
  
  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>YOUR_GPG_PASSPHRASE</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```

## 发布流程

### 1. 更新版本号

```bash
# 更新 pom.xml 中的版本号
# 从 1.0.0-SNAPSHOT 改为 1.0.0
```

### 2. 运行测试

```bash
# 运行所有测试
mvn clean test

# 运行代码质量检查
mvn checkstyle:check

# 运行集成测试
mvn verify
```

### 3. 构建项目

```bash
# 清理并编译
mvn clean compile

# 运行测试
mvn test

# 打包
mvn package
```

### 4. 发布到 Staging

```bash
# 发布到 Sonatype Staging
mvn clean deploy -Prelease
```

### 5. 发布到 Maven Central

1. 登录 [Sonatype OSSRH](https://s01.oss.sonatype.org/)
2. 进入 Staging Repositories
3. 找到你的 staging 仓库
4. 点击 "Close" 按钮
5. 等待验证通过后，点击 "Release" 按钮

### 6. 更新版本号

```bash
# 更新为下一个快照版本
# 从 1.0.0 改为 1.0.1-SNAPSHOT
```

## 自动化发布

### GitHub Actions 配置

创建 `.github/workflows/release.yml`：

```yaml
name: Release

on:
  push:
    tags:
      - 'v*'

jobs:
  release:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        restore-keys: ${{ runner.os }}-m2
    
    - name: Run tests
      run: mvn clean test
    
    - name: Run code quality checks
      run: mvn checkstyle:check
    
    - name: Build and deploy
      env:
        OSSRH_USERNAME: ${{ secrets.OSSRH_USERNAME }}
        OSSRH_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
        GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
        GPG_PRIVATE_KEY: ${{ secrets.GPG_PRIVATE_KEY }}
      run: |
        echo "$GPG_PRIVATE_KEY" | gpg --import
        mvn clean deploy -Prelease
```

### 配置 GitHub Secrets

在 GitHub 仓库设置中添加以下 secrets：

- `OSSRH_USERNAME`: Sonatype 用户名
- `OSSRH_PASSWORD`: Sonatype 密码
- `GPG_PASSPHRASE`: GPG 密钥密码
- `GPG_PRIVATE_KEY`: GPG 私钥

## 版本管理

### 版本号规则

遵循 [语义化版本](https://semver.org/) 规则：

- `MAJOR.MINOR.PATCH`
- 例如：`1.0.0`、`1.1.0`、`1.0.1`

### 发布标签

```bash
# 创建发布标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 推送标签
git push origin v1.0.0
```

## 发布检查清单

### 发布前检查

- [ ] 所有测试通过
- [ ] 代码质量检查通过
- [ ] 文档更新完成
- [ ] 版本号正确
- [ ] 依赖版本正确
- [ ] 许可证文件存在
- [ ] README 更新

### 发布后检查

- [ ] Maven Central 可访问
- [ ] 依赖可正常下载
- [ ] 文档链接正确
- [ ] GitHub Release 创建
- [ ] 版本标签创建

## 故障排除

### 常见问题

1. **GPG 签名失败**
   - 检查 GPG 密钥是否正确配置
   - 确认密码是否正确

2. **认证失败**
   - 检查 Sonatype 用户名密码
   - 确认账号有发布权限

3. **构建失败**
   - 检查 Java 版本
   - 确认所有依赖可访问

4. **发布失败**
   - 检查网络连接
   - 确认 Sonatype 服务状态

### 联系支持

如果遇到问题，可以：

1. 查看 [Sonatype 文档](https://central.sonatype.org/)
2. 提交 GitHub Issue
3. 联系 Sonatype 支持

## 发布历史

| 版本 | 发布日期 | 主要变更 |
|------|----------|----------|
| 1.0.0 | 2024-01-01 | 初始版本发布 |
| 1.0.1 | 待定 | 修复和优化 |

## 相关链接

- [Maven Central](https://search.maven.org/)
- [Sonatype OSSRH](https://s01.oss.sonatype.org/)
- [语义化版本](https://semver.org/)
- [GitHub Releases](https://github.com/kttobug/spring-jpa-smart-query/releases) 