#!/bin/bash
# release.sh - 自动发布脚本
# 使用方法: ./release.sh 1.0.1

set -e

# 检查版本号参数
if [ $# -eq 0 ]; then
    echo "❌ 错误：请提供版本号"
    echo "使用方法: $0 <version>"
    echo "示例: $0 1.0.1"
    exit 1
fi

VERSION=$1

echo "🚀 开始发布版本 $VERSION"

# 检查必要的工具
echo "🔍 检查必要工具..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 未安装，请先安装 Maven"
    exit 1
fi

if ! command -v gpg &> /dev/null; then
    echo "❌ GPG 未安装，请先安装 GPG"
    exit 1
fi

if ! command -v git &> /dev/null; then
    echo "❌ Git 未安装，请先安装 Git"
    exit 1
fi

# 检查 GPG 密钥
echo "🔑 检查 GPG 密钥..."
if ! gpg --list-secret-keys | grep -q "sec"; then
    echo "❌ 未找到 GPG 私钥，请先生成 GPG 密钥"
    echo "运行: gpg --full-generate-key"
    exit 1
fi

# 检查 Maven settings.xml
echo "📝 检查 Maven 配置..."
if [ ! -f ~/.m2/settings.xml ]; then
    echo "❌ 未找到 Maven settings.xml，请先配置"
    exit 1
fi

# 检查工作目录是否干净
echo "🧹 检查工作目录状态..."
if [ -n "$(git status --porcelain)" ]; then
    echo "❌ 工作目录有未提交的更改，请先提交或暂存"
    git status
    exit 1
fi

# 1. 清理并测试
echo "🧪 清理并运行测试..."
mvn clean test

# 2. 更新版本号
echo "🔄 更新版本号到 $VERSION..."
mvn versions:set -DnewVersion=$VERSION
mvn versions:commit

# 3. 验证构建
echo "🔨 验证构建..."
mvn clean compile

# 4. 生成所有必需的构件
echo "📦 生成所有必需的构件..."
mvn clean package -P release

# 5. 验证生成的构件
echo "✅ 验证生成的构件..."
for module in "smart-query-core" "smart-query-spring"; do
    echo "检查模块: $module"
    
    # 检查 JAR 文件
    if [ ! -f "$module/target/$module-$VERSION.jar" ]; then
        echo "❌ 未找到 JAR 文件: $module/target/$module-$VERSION.jar"
        exit 1
    fi
    
    # 检查源码 JAR
    if [ ! -f "$module/target/$module-$VERSION-sources.jar" ]; then
        echo "❌ 未找到源码 JAR: $module/target/$module-$VERSION-sources.jar"
        exit 1
    fi
    
    # 检查 Javadoc JAR
    if [ ! -f "$module/target/$module-$VERSION-javadoc.jar" ]; then
        echo "❌ 未找到 Javadoc JAR: $module/target/$module-$VERSION-javadoc.jar"
        exit 1
    fi
    
    # 检查 POM 文件
    if [ ! -f "$module/target/$module-$VERSION.pom" ]; then
        echo "❌ 未找到 POM 文件: $module/target/$module-$VERSION.pom"
        exit 1
    fi
    
    echo "✅ 模块 $module 的构件检查通过"
done

# 6. 提交版本更改
echo "📝 提交版本更改..."
git add .
git commit -m "Release version $VERSION"
git tag -a v$VERSION -m "Release version $VERSION"

# 7. 发布到 Maven Central
echo "🚀 发布到 Maven Central..."
echo "请输入您的 GPG 密码进行签名..."
mvn clean deploy -P release

# 8. 推送到 GitHub
echo "🔄 推送到 GitHub..."
git push origin main
git push origin v$VERSION

echo ""
echo "✅ 发布完成！"
echo ""
echo "📋 发布信息:"
echo "   版本: $VERSION"
echo "   模块: smart-query-core, smart-query-spring"
echo "   Git 标签: v$VERSION"
echo ""
echo "🔗 下一步:"
echo "   1. 查看发布状态: https://central.sonatype.com/"
echo "   2. 等待 10-30 分钟后，在 Maven Central 中搜索: https://search.maven.org/"
echo "   3. 验证构件是否可用: https://mvnrepository.com/artifact/io.github.kttobug"
echo ""
echo "🎉 恭喜！您的项目已成功发布到 Maven Central！" 