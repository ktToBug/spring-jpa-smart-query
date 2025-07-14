#!/bin/bash
# setup-maven.sh - 设置 Maven 配置脚本

set -e

echo "🛠️  设置 Maven 发布配置"

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 未安装，请先安装 Maven"
    exit 1
fi

# 创建 .m2 目录
mkdir -p ~/.m2

# 备份现有的 settings.xml（如果存在）
if [ -f ~/.m2/settings.xml ]; then
    echo "📋 备份现有的 settings.xml..."
    cp ~/.m2/settings.xml ~/.m2/settings.xml.backup.$(date +%Y%m%d_%H%M%S)
fi

# 提示用户输入配置信息
echo "📝 请输入以下配置信息："
echo ""

read -p "Sonatype 用户名: " SONATYPE_USERNAME
read -s -p "Sonatype 密码: " SONATYPE_PASSWORD
echo ""
read -s -p "GPG 密钥密码: " GPG_PASSPHRASE
echo ""

# 生成 settings.xml
echo "📄 生成 settings.xml..."
cat > ~/.m2/settings.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
  
  <servers>
    <!-- Sonatype Central Portal 服务器配置 -->
    <server>
      <id>central</id>
      <username>${SONATYPE_USERNAME}</username>
      <password>${SONATYPE_PASSWORD}</password>
    </server>
  </servers>
  
  <profiles>
    <!-- 发布配置 -->
    <profile>
      <id>release</id>
      <properties>
        <!-- GPG 签名配置 -->
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>${GPG_PASSPHRASE}</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
  
  <!-- 激活 release profile -->
  <activeProfiles>
    <activeProfile>release</activeProfile>
  </activeProfiles>
  
</settings>
EOF

# 设置文件权限
chmod 600 ~/.m2/settings.xml

echo "✅ Maven 配置完成！"
echo ""
echo "📋 配置文件位置: ~/.m2/settings.xml"
echo "🔒 已设置适当的文件权限 (600)"
echo ""
echo "🔑 接下来请确保您的 GPG 密钥已正确配置："
echo "   1. 检查 GPG 密钥: gpg --list-secret-keys"
echo "   2. 如果没有密钥，生成新的: gpg --full-generate-key"
echo "   3. 导出公钥到密钥服务器:"
echo "      gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID"
echo "      gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID"
echo "      gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID"
echo ""
echo "🚀 配置完成后，您可以使用 ./release.sh 1.0.0 来发布项目" 