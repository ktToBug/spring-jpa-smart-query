#!/bin/bash
# setup.sh - 简单的配置脚本

echo "🔧 Spring JPA Smart Query 配置助手"
echo "=================================="

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

echo "📝 请输入配置信息（密码输入时不会显示）："
echo ""

# 使用 https://central.sonatype.com 生成的 用户名 / 密码
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
    <server>
      <id>ossrh</id>
      <username>${SONATYPE_USERNAME}</username>
      <password>${SONATYPE_PASSWORD}</password>
    </server>
    <server>
      <id>gpg.passphrase</id>
      <passphrase>${GPG_PASSPHRASE}</passphrase>
    </server>
  </servers>
  
  <profiles>
    <profile>
      <id>release</id>
      <properties>
        <gpg.executable>gpg</gpg.executable>
      </properties>
    </profile>
  </profiles>
  
  <activeProfiles>
    <activeProfile>release</activeProfile>
  </activeProfiles>
  
</settings>
EOF

# 设置文件权限
chmod 600 ~/.m2/settings.xml

echo "✅ 配置完成！"
echo ""
echo "📋 配置文件位置: ~/.m2/settings.xml"
echo "🔒 已设置适当的文件权限 (600)"
echo ""
echo "🚀 现在您可以使用以下命令发布："
echo "   mvn clean deploy -P release -DskipTests"
echo ""
echo "�� 详细说明请查看：发布指南.md" 