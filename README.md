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

### 原型补充说明（2026-04-13）
以下几点是用户基于墨刀原型补充的真实产品语义，后续实现时必须以此为准：

1. **Night 页面应为暗色主题**：
   - 墨刀原型中未完整表现深色视觉，但实际需求是 Night 页面整体应切换为暗色。
   - `Day/Night` 开关不仅影响表盘展示区间，也应影响整体页面主题与表盘配色逻辑。

2. **页面流转不是一步直达**：
   - 图 2 `empty-day-MAIN` 点击底部 `SET` 后，进入图 3 `empty-day-set`。
   - 图 3 `empty-day-set` 再点击左侧 `Set Project` 后，进入图 4 `empty-day-setproject`。
   - 因此 `SET` 更像“进入设置态/编辑态”，而 `Set Project` 才是“进入项目录入表单”。

3. **左上角铅笔图标不是编辑入口**：
   - 左上角的小图标用于可视化描述用户当前行为，不代表“编辑”功能。
   - 例如用户上午开启“写作业”任务时，可以展示铅笔图标。
   - 后续实现中，这个图标应理解为根据任务内容自动匹配、调取或生成的行为图标，而不是一个可点击的编辑按钮。

## 本地开发说明（回家后无缝续接）

### 1. 获取最新代码
```bash
git clone https://github.com/ghostLLC/visible-time-manager.git
cd visible-time-manager/vtm
```

如果你家里的电脑之前已经拉过仓库，则直接：
```bash
git pull origin main
```

### 2. 推荐开发环境
- **Android Studio**：建议使用较新的稳定版（优先 Hedgehog / Iguana 及以上）
- **JDK**：使用 Android Studio 自带 JDK 或 JDK 17
- **Android SDK**：
  - Compile SDK / Target SDK：34
  - Min SDK：26
- **Gradle**：使用项目自带配置同步即可，不要手动乱改版本

### 3. 启动步骤
1. 用 Android Studio 打开 `visible-time-manager/vtm`
2. 等待首次 Gradle Sync 完成
3. 若提示缺失 SDK，按 IDE 提示安装 Android 14（API 34）相关组件
4. 连接真机或启动模拟器
5. 运行 `app` 模块

### 4. 当前开发重点（到 2026-04-14）
目前项目已经不只是初始原型，主页与设置流转已经做了多轮细化，重点状态如下：

- 首页已区分 **Day / Night** 两种时间视图
- 表盘中心只保留浅色时间提示：
  - Day：`6:00-18:00`
  - Night：`18:00-6:00`
- 首页表盘下方改成了 **左右滑动双页**：
  - 第 1 页：`Current Project / Next Project`
  - 第 2 页：`Today timeline`
- `SET` 不再直接进入录入表单，而是先进入中间过渡态
- `Set Project` 负责进入项目录入表单
- 已补上 `Delete Project` 入口和删除流程
- `Set project` 页面已做紧凑布局，目的是尽量首屏看完主要内容
- 时间选择器已经做过一轮吸附优化，但这一块仍然属于后续需要继续微调手感的区域

### 5. 你回家后最可能遇到的不是代码问题，而是环境问题
如果回家打开项目后不能立刻运行，优先检查这几项：

1. **Android SDK 34 是否安装**
2. **Android Studio 是否完成 Gradle Sync**
3. **JDK 是否为 17 或 IDE 自带版本**
4. **真机 USB 调试是否开启**
5. **是否误删 / 缺失 Gradle wrapper 相关文件**

### 6. 已知说明
- 当前仓库主分支已经推送到 GitHub：
  - `https://github.com/ghostLLC/visible-time-manager`
- 当前有一轮与首页布局、SetProject 表单、时间滚轮、删除项目流程相关的连续改动
- 如果回家后要继续开发，建议**先直接运行看现状，再改 UI**，不要一上来先改依赖和版本

### 7. 建议的续接顺序
回家后建议按这个顺序继续：

1. 先成功同步并运行项目
2. 真机确认当前 Day / Night、双页切换、SetProject、Delete Project 流程是否正常
3. 再继续微调时间滚轮吸附手感与 SetProject 空间布局
4. 最后再考虑数据持久化（Room / ViewModel）之类的结构升级

