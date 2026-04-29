# Visible Time Manager (VTM)

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9-purple" />
  <img src="https://img.shields.io/badge/Compose_BOM-2023.08-blue" />
  <img src="https://img.shields.io/badge/Material_3-✓-green" />
  <img src="https://img.shields.io/badge/Min_SDK-26-orange" />
  <img src="https://img.shields.io/badge/Gradle-8.2-lightgrey" />
</p>

一个基于 **Kotlin + Jetpack Compose + Material 3** 的 Android 时间可视化应用。

VTM 不走传统列表式待办，而是用 **12 小时双环钟面** 让你以最直观的方式观察当前半天的时间分配——日间/夜间双窗口自动切换，用滑动浏览时间线，用日历回溯和规划。

---

## ✨ 核心特性

### 🌓 日/夜双模式
- 一天拆为 **Day**（06:00–18:00）和 **Night**（18:00–次日06:00）两个 12h 窗口
- 模式切换按钮一键切换，spring 弹性动画过渡颜色与弧段
- 启动时根据系统时间自动匹配对应模式

### 🕐 12 小时表盘（ClockFace）
- Compose Canvas 自定义绘制，1–12 数字刻度 + 实时时针
- 项目时间段以彩色圆弧显示在外环，内环显示当前/下一个项目
- 日/夜切换时主次色、主题色、页面内容平滑动画过渡

### 📅 日历页 — 时间线视图
- 紧凑月份网格，日期圆点标注活动数
- 选中日期下方嵌入 **24h 迷你时间线条**，彩色段直观展示任务分布
- 日间（6:00–18:00）/ 夜间分界线标注 D/N
- 日期摘要：`4h 已安排 · 20h 空闲`
- 快捷添加按钮，一键跳转 Clock 页创建任务

### ⏱ 时钟页 — 核心操控台
- 日期导航条：‹ 前一天 / 日期 / › 后一天 + 日历快捷入口
- Current / Next 项目概览卡
- 横向双页：项目概览 ↔ 今日时间线
- 任务左滑删除手势

### 👤 Me 页 — 个人设置
- 身份头 + 今日概要统计
- 内联策略选择（Steady / Balanced / Sprint）
- 内联设置开关（Wi-Fi 同步、自动归档、深色模式、语言切换）

### 🎨 项目编辑器
- 12 色精选调色板，色相均匀覆盖全光谱
- 选中色块带白色 ✓ 勾选标记 + spring 缩放动画
- 吸附式滚轮时间选择，自动避让已占用时段
- 实时冲突检测，阻止重叠保存

### 🔔 前台计时通知
- 活跃任务自动启动前台 Service 通知
- 通知栏实时显示当前项目与剩余时间
- 任务结束后自动停止通知

### 🌐 多语言支持
- 英文 / 简体中文 / 繁体中文三语言
- 全界面文案统一走 `AppLanguage` + `CompositionLocal` 体系

### 🔄 三页互通
- Clock ↔ Calendar ↔ Me 通过 HorizontalPager 滑动切换
- 底栏图标导航，页面间数据双向联动
- `beyondViewportPageCount` 预组合，切换无卡顿

---

## 📱 界面结构

```
┌─────────────────────────────────┐
│          日期导航条              │  ← ‹ 4/24 周四 ›  日历 ›
├─────────────────────────────────┤
│         随机惜时短句             │
├─────────────────────────────────┤
│                                 │
│      🕐 12h 双环钟面             │  ← 外环任务 / 内环时间线
│                                 │
├─────────────────────────────────┤
│  Day/Night 切换  │  SET 按钮    │
├─────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐    │
│  │ Current   │  │ Timeline │    │  ← 横向滑动切换
│  │ Next      │  │ Today    │    │
│  └──────────┘  └──────────┘    │
├─────────────────────────────────┤
│  📅 Calendar  🕐 Clock  👤 Me   │  ← 底栏导航
└─────────────────────────────────┘
```

---

## 🗂 项目结构

```
app/src/main/java/com/vtm/app/
├── MainActivity.kt              # 应用入口，初始化 Day/Night 模式 + 启动预热
├── model/
│   └── TimeTask.kt              # 时间任务数据结构
├── components/
│   └── ClockFace.kt             # 12h 双环钟面自定义绘制 + 手势处理
├── service/
│   └── TimerForegroundService.kt # 前台计时通知服务
├── ui/
│   ├── MainScreen.kt            # 主界面（~5000 行，三页 + 编辑器 + 动画系统）
│   ├── LanguageStrings.kt       # 多语言文案体系（英/简中/繁中）
│   └── theme/
│       ├── Color.kt             # 主题色 + 12 色调色板
│       └── Theme.kt             # 亮/暗 Material 3 主题
└── res/
    ├── drawable/
    │   └── ic_timer_notification.xml  # 通知图标
    └── mipmap-anydpi-v26/
        ├── ic_launcher.xml
        └── ic_launcher_round.xml
```

---

## 🛠 技术栈

| 依赖 | 版本 | 用途 |
|------|------|------|
| Kotlin | 1.9.0 | 主语言 |
| Jetpack Compose (BOM) | 2023.08 | UI 框架 |
| Compose Compiler | 1.5.1 | Composable 编译 |
| Material 3 | BOM 托管 | 设计系统 |
| Compose Animation | BOM 托管 | Spring 动画、页面转场 |
| SharedPreferences + JSON | — | 本地持久化 |
| Foreground Service | — | 前台计时通知 |

- **Compile SDK**: 34
- **Min SDK**: 26
- **Target SDK**: 34
- **JDK**: 17

---

## 🚀 运行项目

### 方式一：Android Studio（推荐）

1. 克隆仓库：
   ```bash
   git clone https://github.com/ghostLLC/visible-time-manager.git
   ```
2. 用 Android Studio 打开 `visible-time-manager`（即仓库根目录，Android 项目根目录）
3. 等待 Gradle Sync 完成（首次打开 Android Studio 会自动生成 `gradlew` 脚本）
4. 连接真机或启动模拟器（API 26+）
5. 运行 `app` 模块

### 方式二：命令行

```bash
cd visible-time-manager
./gradlew assembleDebug
```

> 仓库包含 `gradle/wrapper/gradle-wrapper.properties` 配置，首次用 Android Studio 打开后会自动生成 `gradlew` / `gradlew.bat` 脚本。

---

## 📦 打包 APK

### Debug APK（最快，适合测试）

Android Studio → **Build > Build Bundle(s) / APK(s) > Build APK(s)**

输出路径：`app/build/outputs/apk/debug/app-debug.apk`

### Release APK（签名版，适合分发）

Android Studio → **Build > Generate Signed Bundle / APK...** → 选择 APK → 创建或选择 Keystore → Release 变体

> ⚠️ Release 签名信息务必妥善保存，后续更新同一应用必须使用相同签名。

---

## 📋 已知限制

| 项目 | 状态 | 说明 |
|------|------|------|
| ViewModel / Repository 分层 | ❌ 未做 | 状态管理以 Compose 内嵌 state 为主 |
| Room 数据库 | ❌ 未做 | 当前使用 SharedPreferences + JSON 持久化 |
| 单元测试 / UI 测试 | ❌ 未做 | 暂无自动化测试覆盖 |
| CI/CD | ❌ 未做 | 无自动化构建与发布流程 |
| 正式发布签名 | ❌ 未做 | 仅有 debug 签名 |
| Coin-Flip 拖拽翻转 | 🔬 实验性 | 代码已实现但默认禁用（`shouldEnableCoinFlip = false`） |

---

## 🗺 后续迭代方向

1. **架构重构**：拆分 MainScreen.kt（~5000 行）为多个 Composable + ViewModel
2. **Room 数据库**：替代 SharedPreferences，支持复杂查询和高效序列化
3. **提醒增强**：到点提醒、番茄钟模式、重复任务
4. **统计与回顾**：周/月维度的时间分配分析图表
5. **数据导入/导出**：支持 JSON/CSV 导出时间记录
6. **桌面 Widget**：显示当前/下一个任务

---

## 📄 License

MIT

---

## 🔗 仓库

[https://github.com/ghostLLC/visible-time-manager](https://github.com/ghostLLC/visible-time-manager)
