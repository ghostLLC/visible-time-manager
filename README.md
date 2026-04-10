# Visible Time Manager (VTM)

## 项目简介
这是一个基于 **Kotlin + Jetpack Compose** 开发的轻量级 Android 应用程序，旨在帮助用户通过直观的 12 小时制表盘（ClockFace）可视化地管理和记录每天的时间。

## 当前开发进度 (AI 接力上下文)
**给接力 AI 的提示：请读取此文档以了解当前的项目结构和下一步的开发计划。**

目前项目已经完成了**阶段一**的从 0 到 1 基础架构搭建，并根据原型图（PDF提取）实现了核心 UI 组件。

### 已实现的功能
1. **基础架构**：完整的 Android Gradle 项目，使用 Jetpack Compose。
2. **核心 UI 布局** ([MainScreen.kt](./app/src/main/java/com/vtm/app/ui/MainScreen.kt))：
   - 动态问候语。
   - 自定义绘制的表盘组件。
   - Day/Night（日夜）模式切换开关（UI 已实现，暂未绑定深色主题切换）。
   - 当前任务（Current Project）和下一个任务（Next Project）的卡片布局。
   - 底部的操作按钮。
3. **核心可视化组件** ([ClockFace.kt](./app/src/main/java/com/vtm/app/components/ClockFace.kt))：
   - 使用 Compose `Canvas` API 手工绘制了 12 小时制的表盘。
   - 实现了根据传入的 `TimeTask` 数据（包含 `startHour`, `startMinute`, `endHour`, `endMinute`, `color`），在表盘上动态绘制对应时间段的彩色扇形（Arc）。
   - 处理了 Day（0-12时）和 Night（12-24时）模式下任务是否应该显示在当前表盘的逻辑。
4. **数据模型** ([TimeTask.kt](./app/src/main/java/com/vtm/app/model/TimeTask.kt))：定义了时间任务的基础数据结构。
5. **主题与颜色** ([Color.kt](./app/src/main/java/com/vtm/app/ui/theme/Color.kt) / [Theme.kt](./app/src/main/java/com/vtm/app/ui/theme/Theme.kt))：定义了日夜模式的基础色值和 Material 3 主题。

### 待实现的功能 (下一步计划)
接下来请按照以下优先级继续开发：

1. **实现 "Set Project" 交互 (高优先级)**：
   - 点击 `Set Project` 或底部的 `SET` 按钮时，弹出一个 BottomSheet 或 Dialog。
   - 弹窗内需要包含：开始时间选择器、结束时间选择器、任务内容输入框（TextField）以及内容类型选择（DropdownMenu）。
2. **状态管理与数据持久化 (中优先级)**：
   - 引入 `ViewModel` 来管理当前的 `tasks` 列表和 `isNightMode` 状态。
   - 引入 **Room Database**，将用户添加的 `TimeTask` 保存到本地 SQLite 数据库中，实现数据的持久化。
3. **时间计算逻辑 (中优先级)**：
   - 完善首页卡片的逻辑：根据当前手机的真实时间，计算并实时显示“当前任务剩余时间（x hour(s) x min(s) left）”以及“下一个任务的开始时间”。
   - 实现顶部问候语的动态更新。
4. **UI 细节打磨 (低优先级)**：
   - 完善 Day/Night 模式切换时全局主题颜色的平滑过渡。
   - 处理跨越 12 点（例如从上午 11 点到下午 2 点）任务在表盘上的显示逻辑（可能需要拆分为两段弧线）。

## 技术栈
- **语言**: Kotlin
- **UI 框架**: Jetpack Compose (Material 3)
- **目标 SDK**: API 34 (Android 14)
- **最低 SDK**: API 26 (Android 8.0)

## 如何运行
1. 使用 Android Studio 打开本项目根目录 (`visible-time-manager/vtm`)。
2. 等待 Gradle 同步完成。
3. 连接 Android 模拟器或真机，点击 `Run` 按钮。
