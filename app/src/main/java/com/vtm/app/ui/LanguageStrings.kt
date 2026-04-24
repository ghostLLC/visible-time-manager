package com.vtm.app.ui

import androidx.compose.runtime.compositionLocalOf

enum class AppLanguage(val code: String, val displayName: String) {
    English("en", "English"),
    SimplifiedChinese("zh-CN", "简体中文"),
    TraditionalChinese("zh-TW", "繁體中文"),
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.English }

// — Language-aware string helpers —

fun AppLanguage.strCalendar() = when (this) {
    AppLanguage.English -> "Calendar"
    AppLanguage.SimplifiedChinese -> "日历"
    AppLanguage.TraditionalChinese -> "日曆"
}

fun AppLanguage.strClock() = when (this) {
    AppLanguage.English -> "Clock"
    AppLanguage.SimplifiedChinese -> "时钟"
    AppLanguage.TraditionalChinese -> "時鐘"
}

fun AppLanguage.strMe() = when (this) {
    AppLanguage.English -> "Me"
    AppLanguage.SimplifiedChinese -> "我的"
    AppLanguage.TraditionalChinese -> "我的"
}

fun AppLanguage.strToday() = when (this) {
    AppLanguage.English -> "Today"
    AppLanguage.SimplifiedChinese -> "今天"
    AppLanguage.TraditionalChinese -> "今天"
}

fun AppLanguage.strItems(count: Int) = when (this) {
    AppLanguage.English -> "${count} item" + (if (count != 1) "s" else "")
    AppLanguage.SimplifiedChinese -> "${count} 项"
    AppLanguage.TraditionalChinese -> "${count} 項"
}

fun AppLanguage.strEmptyDay() = when (this) {
    AppLanguage.English -> "Empty day. Double-tap a gap on the clock to add."
    AppLanguage.SimplifiedChinese -> "空白日，去时钟页双击空档添加安排。"
    AppLanguage.TraditionalChinese -> "空白日，去時鐘頁雙擊空檔添加安排。"
}

fun AppLanguage.strViewClock() = when (this) {
    AppLanguage.English -> "View Clock ›"
    AppLanguage.SimplifiedChinese -> "查看时钟 ›"
    AppLanguage.TraditionalChinese -> "查看時鐘 ›"
}

fun AppLanguage.strWeekday(dayOfWeek: Int) = when (this) {
    AppLanguage.English -> when (dayOfWeek) {
        1 -> "Mon"; 2 -> "Tue"; 3 -> "Wed"; 4 -> "Thu"; 5 -> "Fri"; 6 -> "Sat"; else -> "Sun"
    }
    AppLanguage.SimplifiedChinese -> when (dayOfWeek) {
        1 -> "周一"; 2 -> "周二"; 3 -> "周三"; 4 -> "周四"; 5 -> "周五"; 6 -> "周六"; else -> "周日"
    }
    AppLanguage.TraditionalChinese -> when (dayOfWeek) {
        1 -> "週一"; 2 -> "週二"; 3 -> "週三"; 4 -> "週四"; 5 -> "週五"; 6 -> "週六"; else -> "週日"
    }
}

fun AppLanguage.strWeekdayHeader(dayOfWeek: Int) = when (this) {
    AppLanguage.English -> when (dayOfWeek) {
        1 -> "M"; 2 -> "T"; 3 -> "W"; 4 -> "T"; 5 -> "F"; 6 -> "S"; else -> "S"
    }
    AppLanguage.SimplifiedChinese -> when (dayOfWeek) {
        1 -> "一"; 2 -> "二"; 3 -> "三"; 4 -> "四"; 5 -> "五"; 6 -> "六"; else -> "日"
    }
    AppLanguage.TraditionalChinese -> when (dayOfWeek) {
        1 -> "一"; 2 -> "二"; 3 -> "三"; 4 -> "四"; 5 -> "五"; 6 -> "六"; else -> "日"
    }
}

fun AppLanguage.strMonthYear(year: Int, month: Int) = when (this) {
    AppLanguage.English -> {
        val monthName = java.time.Month.of(month).name.lowercase().replaceFirstChar { it.uppercase() }
        "$monthName $year"
    }
    AppLanguage.SimplifiedChinese -> "${year} 年 ${month} 月"
    AppLanguage.TraditionalChinese -> "${year} 年 ${month} 月"
}

fun AppLanguage.strNoScheduleYet() = when (this) {
    AppLanguage.English -> "No schedule yet"
    AppLanguage.SimplifiedChinese -> "今天还没有安排"
    AppLanguage.TraditionalChinese -> "今天還沒有安排"
}

fun AppLanguage.strActive() = when (this) {
    AppLanguage.English -> "Active"
    AppLanguage.SimplifiedChinese -> "进行中"
    AppLanguage.TraditionalChinese -> "進行中"
}

fun AppLanguage.strNext() = when (this) {
    AppLanguage.English -> "Next"
    AppLanguage.SimplifiedChinese -> "下一个"
    AppLanguage.TraditionalChinese -> "下一個"
}

fun AppLanguage.strStrategy() = when (this) {
    AppLanguage.English -> "Strategy"
    AppLanguage.SimplifiedChinese -> "策略"
    AppLanguage.TraditionalChinese -> "策略"
}

fun AppLanguage.strSettings() = when (this) {
    AppLanguage.English -> "Settings"
    AppLanguage.SimplifiedChinese -> "设置"
    AppLanguage.TraditionalChinese -> "設置"
}

fun AppLanguage.strWifiSync(on: Boolean) = when (this) {
    AppLanguage.English -> if (on) "Wi-Fi only sync" else "Cellular allowed"
    AppLanguage.SimplifiedChinese -> if (on) "更保守的默认时长" else "允许移动网络"
    AppLanguage.TraditionalChinese -> if (on) "更保守的預設時長" else "允許移動網絡"
}

fun AppLanguage.strWifiSyncTitle() = when (this) {
    AppLanguage.English -> "Wi-Fi Only Sync"
    AppLanguage.SimplifiedChinese -> "仅 Wi-Fi 同步"
    AppLanguage.TraditionalChinese -> "僅 Wi-Fi 同步"
}

fun AppLanguage.strAutoArchive(on: Boolean) = when (this) {
    AppLanguage.English -> if (on) "Auto-saved weekly" else "Manual archive"
    AppLanguage.SimplifiedChinese -> if (on) "每周自动保存" else "手动归档"
    AppLanguage.TraditionalChinese -> if (on) "每週自動保存" else "手動歸檔"
}

fun AppLanguage.strAutoArchiveTitle() = when (this) {
    AppLanguage.English -> "Auto Archive"
    AppLanguage.SimplifiedChinese -> "自动归档"
    AppLanguage.TraditionalChinese -> "自動歸檔"
}

fun AppLanguage.strAppearance() = when (this) {
    AppLanguage.English -> "Appearance"
    AppLanguage.SimplifiedChinese -> "外观"
    AppLanguage.TraditionalChinese -> "外觀"
}

fun AppLanguage.strDayMode() = when (this) {
    AppLanguage.English -> "Day mode"
    AppLanguage.SimplifiedChinese -> "日间模式"
    AppLanguage.TraditionalChinese -> "日間模式"
}

fun AppLanguage.strNightMode() = when (this) {
    AppLanguage.English -> "Night mode"
    AppLanguage.SimplifiedChinese -> "夜间模式"
    AppLanguage.TraditionalChinese -> "夜間模式"
}

fun AppLanguage.strLanguage() = when (this) {
    AppLanguage.English -> "Language"
    AppLanguage.SimplifiedChinese -> "语言"
    AppLanguage.TraditionalChinese -> "語言"
}

fun AppLanguage.strWifiLabel(on: Boolean) = when (this) {
    AppLanguage.English -> if (on) "Wi-Fi" else "Cell"
    AppLanguage.SimplifiedChinese -> if (on) "Wi-Fi" else "蜂窝"
    AppLanguage.TraditionalChinese -> if (on) "Wi-Fi" else "蜂窩"
}

fun AppLanguage.strWeeklyManual(on: Boolean) = when (this) {
    AppLanguage.English -> if (on) "Weekly" else "Manual"
    AppLanguage.SimplifiedChinese -> if (on) "每周" else "手动"
    AppLanguage.TraditionalChinese -> if (on) "每週" else "手動"
}

fun AppLanguage.strHourLabel() = when (this) {
    AppLanguage.English -> "H"
    AppLanguage.SimplifiedChinese -> "时"
    AppLanguage.TraditionalChinese -> "時"
}

fun AppLanguage.strMinuteLabel() = when (this) {
    AppLanguage.English -> "M"
    AppLanguage.SimplifiedChinese -> "分"
    AppLanguage.TraditionalChinese -> "分"
}

fun AppLanguage.strSwipeToDelete() = when (this) {
    AppLanguage.English -> "Swipe left to delete"
    AppLanguage.SimplifiedChinese -> "左滑删除"
    AppLanguage.TraditionalChinese -> "左滑刪除"
}

fun AppLanguage.strNoTasksEmpty() = when (this) {
    AppLanguage.English -> "Nothing planned yet"
    AppLanguage.SimplifiedChinese -> "还没有安排"
    AppLanguage.TraditionalChinese -> "還沒有安排"
}

fun AppLanguage.strAddFirstTask() = when (this) {
    AppLanguage.English -> "Tap + to add your first task"
    AppLanguage.SimplifiedChinese -> "点击 + 添加第一个安排"
    AppLanguage.TraditionalChinese -> "點擊 + 添加第一個安排"
}

fun AppLanguage.strTimerRunning() = when (this) {
    AppLanguage.English -> "Timer running"
    AppLanguage.SimplifiedChinese -> "计时中"
    AppLanguage.TraditionalChinese -> "計時中"
}

fun AppLanguage.strDelete() = when (this) {
    AppLanguage.English -> "Delete"
    AppLanguage.SimplifiedChinese -> "删除"
    AppLanguage.TraditionalChinese -> "刪除"
}

fun AppLanguage.strPlanned() = when (this) {
    AppLanguage.English -> "Planned"
    AppLanguage.SimplifiedChinese -> "已安排"
    AppLanguage.TraditionalChinese -> "已安排"
}

fun AppLanguage.strFree() = when (this) {
    AppLanguage.English -> "Free"
    AppLanguage.SimplifiedChinese -> "空闲"
    AppLanguage.TraditionalChinese -> "空閒"
}

fun AppLanguage.strAddTask() = when (this) {
    AppLanguage.English -> "Add"
    AppLanguage.SimplifiedChinese -> "添加"
    AppLanguage.TraditionalChinese -> "添加"
}

fun AppLanguage.strViewCalendar() = when (this) {
    AppLanguage.English -> "Calendar ›"
    AppLanguage.SimplifiedChinese -> "日历 ›"
    AppLanguage.TraditionalChinese -> "日曆 ›"
}

fun AppLanguage.strDay() = when (this) {
    AppLanguage.English -> "Day"
    AppLanguage.SimplifiedChinese -> "日间"
    AppLanguage.TraditionalChinese -> "日間"
}

fun AppLanguage.strNight() = when (this) {
    AppLanguage.English -> "Night"
    AppLanguage.SimplifiedChinese -> "夜间"
    AppLanguage.TraditionalChinese -> "夜間"
}

fun AppLanguage.strDayNight() = when (this) {
    AppLanguage.English -> "Day + Night"
    AppLanguage.SimplifiedChinese -> "日间 + 夜间"
    AppLanguage.TraditionalChinese -> "日間 + 夜間"
}

fun AppLanguage.strColorLabel() = when (this) {
    AppLanguage.English -> "Color"
    AppLanguage.SimplifiedChinese -> "颜色"
    AppLanguage.TraditionalChinese -> "顏色"
}

fun AppLanguage.strColorSelected() = when (this) {
    AppLanguage.English -> "Selected"
    AppLanguage.SimplifiedChinese -> "已选"
    AppLanguage.TraditionalChinese -> "已選"
}
