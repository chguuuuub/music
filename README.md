# 灵境·自动作曲 Android

一个离线优先的文字意境算法作曲应用。Android WebView 加载本地 HTML/JavaScript，不依赖在线 CDN。

## 功能（首版）
- 中文意境输入，按文本特征生成情绪、速度与调式
- 算法生成旋律、和弦、低音、节奏等声部
- 钢琴、弦乐、长笛、吉他、低音、钟琴、合成铺底、打击乐
- 播放、暂停（当前暂停后从头播放）、停止
- 导出 WAV 音频与 MIDI 文件
- Android 系统文件选择器保存导出文件

## 构建 APK
需要 JDK 17、Android SDK 35、Gradle 8.9：
```bash
gradle assembleDebug
```
APK 输出：`app/build/outputs/apk/debug/app-debug.apk`

仓库已配置 GitHub Actions：推送到 main 或 android-apk-v1 后自动构建，并上传 `lingjing-music-debug-apk` artifact。

## AI 音乐生成
当前版本的本地算法引擎可独立工作。AI 音乐生成需要后端代理与服务商 API；不要将 API 密钥写入 HTML、APK 或公开仓库。后续可在服务端实现 Provider 接口。

## 版本说明
首版提供 WAV 与 MIDI 导出。MP3 编码尚未集成，不会将 WAV 文件伪装成 MP3。
