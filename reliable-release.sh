#!/bin/bash
# reliable-release.sh - 可靠的发布脚本
# 使用方法: ./reliable-release.sh 1.0.1

set -e

echo "🚀 Spring JPA Smart Query 可靠发布助手"
echo "========================================"

# 检查版本号参数
if [ $# -eq 0 ]; then
    echo "❌ 请提供版本号"
    echo "使用方法: $0 <版本号>"
    echo "示例: $0 1.0.1"
    exit 1
fi

VERSION=$1
echo "📋 准备发布版本: $VERSION"
echo ""

# 步骤 1: 检查工具
echo "步骤 1/6: 检查必要工具..."
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 未安装"
    exit 1
fi

if ! command -v gpg &> /dev/null; then
    echo "❌ GPG 未安装"
    exit 1
fi

if ! command -v git &> /dev/null; then
    echo "❌ Git 未安装"
    exit 1
fi
echo "✅ 工具检查通过"
echo ""

# 步骤 2: 检查 GPG 密钥
echo "步骤 2/6: 检查 GPG 密钥..."
if ! gpg --list-secret-keys | grep -q "sec"; then
    echo "❌ 未找到 GPG 私钥"
    echo "请先运行: gpg --full-generate-key"
    exit 1
fi
echo "✅ GPG 密钥存在"
echo ""

# 步骤 3: 重置 GPG 环境
echo "步骤 3/6: 重置 GPG 环境..."
echo "停止 GPG 代理..."
gpgconf --kill gpg-agent 2>/dev/null || true
sleep 2

echo "启动新的 GPG 代理..."
gpg-agent --daemon --use-standard-socket --allow-loopback-pinentry

echo "设置 GPG 环境变量..."
export GPG_TTY=$(tty)
export GPG_AGENT_INFO=""
echo "✅ GPG 环境重置完成"
echo ""

# 步骤 4: 清理和测试
echo "步骤 4/6: 清理项目并运行测试..."
echo "执行: mvn clean test"
mvn clean test
echo "✅ 测试通过"
echo ""

# 步骤 5: 更新版本号
echo "步骤 5/6: 更新版本号到 $VERSION..."
mvn versions:set -DnewVersion=$VERSION -q
mvn versions:commit -q
echo "✅ 版本号已更新"
echo ""

# 步骤 6: 发布到 Maven Central
echo "步骤 6/6: 发布到 Maven Central..."
echo "⚠️  即将发布到 Maven Central"
echo ""
echo "📝 选择密码输入方式："
echo "1. 环境变量方式（推荐，最可靠）"
echo "2. 交互式输入方式"
echo "3. 跳过签名（仅用于测试）"
echo ""

echo "🔐 使用环境变量方式（最可靠）..."
read -s -p "请输入 GPG 密码: " gpg_passphrase
echo ""
export GPG_PASSPHRASE="$gpg_passphrase"
echo "✅ 已设置 GPG 密码环境变量"
echo ""
echo "执行: mvn clean deploy -P release"
mvn clean deploy -P release

echo ""
echo "🎉 发布完成！"
echo ""
echo "📋 发布信息:"
echo "   版本: $VERSION"
echo "   模块: smart-query-core, smart-query-spring"
echo ""
echo "🔗 接下来:"
echo "   1. 提交版本更改: git add . && git commit -m \"Release version $VERSION\""
echo "   2. 创建标签: git tag -a v$VERSION -m \"Release version $VERSION\""
echo "   3. 推送到 GitHub: git push origin main && git push origin v$VERSION"
echo "   4. 查看发布状态: https://central.sonatype.com/"
echo "   5. 等待 10-30 分钟后检查: https://search.maven.org/"
echo ""
echo "✨ 恭喜！您的项目已发布到 Maven Central！"

# 清理环境变量
unset GPG_PASSPHRASE 