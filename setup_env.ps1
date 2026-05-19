Write-Host "=== Android SDK 配置脚本 ===" -ForegroundColor Cyan

# 设置路径
$ANDROID_HOME = "C:\Android\Sdk"
$JAVA_HOME = "C:\Users\德斌\.trae-cn\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64"

# 创建目录
Write-Host "1. 创建目录结构..." -ForegroundColor Yellow
New-Item -ItemType Directory -Path "$ANDROID_HOME\cmdline-tools\latest" -Force | Out-Null
New-Item -ItemType Directory -Path "$ANDROID_HOME\platform-tools" -Force | Out-Null
New-Item -ItemType Directory -Path "$ANDROID_HOME\platforms\android-34" -Force | Out-Null
New-Item -ItemType Directory -Path "$ANDROID_HOME\build-tools\34.0.0" -Force | Out-Null

# 设置环境变量
Write-Host "2. 设置环境变量..." -ForegroundColor Yellow
[Environment]::SetEnvironmentVariable("ANDROID_HOME", $ANDROID_HOME, "User")
[Environment]::SetEnvironmentVariable("JAVA_HOME", $JAVA_HOME, "User")

$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$ANDROID_HOME\platform-tools;$ANDROID_HOME\cmdline-tools\latest\bin;$JAVA_HOME\bin;$currentPath"
[Environment]::SetEnvironmentVariable("Path", $newPath, "User")

Write-Host "3. 下载必要文件..." -ForegroundColor Yellow
Write-Host "请手动下载以下文件并解压到对应目录：" -ForegroundColor Red
Write-Host "   - Command Line Tools: https://dl.google.com/android/repository/commandlinetools-win-10406996_latest.zip"
Write-Host "     解压到: $ANDROID_HOME\cmdline-tools\latest\"
Write-Host ""
Write-Host "   - Platform Tools: https://dl.google.com/android/repository/platform-tools-latest-windows.zip"
Write-Host "     解压到: $ANDROID_HOME\platform-tools\"
Write-Host ""
Write-Host "   - Android 34 Platform: https://dl.google.com/android/repository/platforms_r34.zip"
Write-Host "     解压到: $ANDROID_HOME\platforms\android-34\"
Write-Host ""
Write-Host "   - Build Tools 34.0.0: https://dl.google.com/android/repository/build-tools_r34-windows.zip"
Write-Host "     解压到: $ANDROID_HOME\build-tools\34.0.0\"

Write-Host ""
Write-Host "=== 配置完成 ===" -ForegroundColor Green
Write-Host "请打开新的 PowerShell 窗口，然后运行:"
Write-Host "cd d:\英雄杀"
Write-Host "gradlew assembleDebug"