package com.vtm.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent

import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween




import androidx.compose.runtime.CompositionLocalProvider



import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith






import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas




import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth






import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.aspectRatio





import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton

import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos

import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb



import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity




import androidx.compose.ui.text.font.FontFamily


import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow



import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import com.vtm.app.components.ClockEmptySlot
import com.vtm.app.components.ClockModeTransitionStyle
import com.vtm.app.components.ClockFace
import com.vtm.app.components.ClockFaceRenderQuality



import com.vtm.app.model.TimeTask
import com.vtm.app.service.TimerForegroundService
import com.vtm.app.ui.theme.ClockBlue
import com.vtm.app.ui.theme.ClockGreen
import com.vtm.app.ui.theme.ClockPurple
import com.vtm.app.ui.theme.ClockRed
import com.vtm.app.ui.theme.ClockTeal
import com.vtm.app.ui.theme.ClockYellow
import com.vtm.app.ui.theme.VTMTheme
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.roundToInt
import org.json.JSONArray
import org.json.JSONObject



private enum class MainScreenMode {
    Normal,
    SetProject,
}

private val taskColorPalette = listOf(
    ClockBlue,           // 0xFF5B8DEF  Blue
    ClockRed,            // 0xFFFF6B6B  Coral Red
    ClockGreen,          // 0xFF38B27D  Green
    ClockYellow,         // 0xFFF4B63F  Amber
    ClockPurple,         // 0xFF8B6BFF  Violet
    ClockTeal,           // 0xFF29B8C8  Teal
    Color(0xFFF97316),   // Orange
    Color(0xFFEC4899),   // Rose
    Color(0xFF6366F1),   // Indigo
    Color(0xFF84CC16),   // Lime
    Color(0xFFA78BFA),   // Lavender
    Color(0xFF14B8A6),   // Emerald
)
private enum class BottomNavPage(val index: Int) {
    Calendar(0),
    Clock(1),
    Profile(2);

    companion object {
        fun fromIndex(index: Int) = entries.firstOrNull { it.index == index } ?: Clock
    }
}



private data class BottomNavItem(
    val page: BottomNavPage,
    val label: String,
    val icon: ImageVector,
)


private val bottomNavItems = listOf(
    BottomNavItem(
        page = BottomNavPage.Calendar,
        label = "Calendar",
        icon = Icons.Filled.DateRange,
    ),
    BottomNavItem(
        page = BottomNavPage.Clock,
        label = "Clock",
        icon = Icons.Filled.AccessTime,
    ),
    BottomNavItem(
        page = BottomNavPage.Profile,
        label = "Me",
        icon = Icons.Filled.Person,
    ),
)

private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")





private fun MainScreenMode.transitionLevel(): Int = when (this) {
    MainScreenMode.Normal -> 0
    MainScreenMode.SetProject -> 1
}

@Composable
private fun rememberMinuteClockTime(isActive: Boolean): LocalTime {
    val lifecycleOwner = LocalLifecycleOwner.current
    var now by remember { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }

    DisposableEffect(lifecycleOwner, isActive) {
        if (!isActive) {
            onDispose { }
        } else {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START || event == Lifecycle.Event.ON_RESUME) {
                    now = LocalTime.now().withSecond(0).withNano(0)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    LaunchedEffect(isActive) {
        if (!isActive) return@LaunchedEffect

        while (true) {
            val current = LocalTime.now()
            now = current.withSecond(0).withNano(0)
            val nextMinute = now.plusMinutes(1)
            val delayMillis = Duration.between(current, nextMinute).toMillis().coerceAtLeast(200L)
            delay(delayMillis)
        }
    }

    return now
}



private val supportQuotes = listOf(

    "Lost time is never found again. — Franklin",
    "Time stays long enough for those who use it. — Leonardo da Vinci",
    "The future depends on what you do today. — Gandhi",
    "You may delay, but time will not. — Franklin",
    "Well used time is the step to a fuller life. — Adapted",
    "Take time while time remains. — Hemingway",
    "The shorter way to do many things is to do one thing at a time. — Mozart",
    "Light tomorrow begins with a focused night. — Adapted",
    "Time is the most valuable thing we spend. — Theophrastus",
    "Small nightly hours shape the next bright day. — Adapted",
)





private fun splitSupportQuote(quote: String): Pair<String, String?> {
    val separator = " — "
    return if (quote.contains(separator)) {
        val parts = quote.split(separator, limit = 2)
        parts[0] to parts.getOrNull(1)
    } else {
        quote to null
    }
}

private fun formatDurationLabel(totalMinutes: Int): String {
    val safeMinutes = totalMinutes.coerceAtLeast(0)
    val hours = safeMinutes / 60
    val minutes = safeMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}




@Composable
private fun neutralFilledButtonContainerColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    return if (isDarkMode) {
        colorScheme.surfaceVariant.copy(alpha = 0.9f)
    } else {
        colorScheme.surfaceVariant
    }
}

@Composable
private fun neutralFilledButtonContentColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    return if (isDarkMode) {
        colorScheme.onSurface.copy(alpha = 0.94f)
    } else {
        colorScheme.onSurface
    }
}

@Composable
private fun neutralOutlinedButtonContainerColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    return if (isDarkMode) {
        colorScheme.surfaceVariant.copy(alpha = 0.34f)
    } else {
        colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }
}

@Composable
private fun neutralOutlinedButtonContentColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    return if (isDarkMode) {
        colorScheme.onSurface.copy(alpha = 0.9f)
    } else {
        colorScheme.onSurface.copy(alpha = 0.88f)
    }
}

@Composable
private fun neutralOutlinedBorderColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    return if (isDarkMode) {
        colorScheme.outline.copy(alpha = 0.46f)
    } else {
        colorScheme.outline.copy(alpha = 0.3f)
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(


    isNightMode: Boolean,
    onNightModeChange: (Boolean) -> Unit,
    startupWarmupActive: Boolean = false,
) {

    val today = remember { LocalDate.now() }
    val context = LocalContext.current
    val taskStorage = remember(context) {
        context.getSharedPreferences("vtm_tasks", android.content.Context.MODE_PRIVATE)
    }
    var selectedDateEpochDay by rememberSaveable { mutableStateOf(today.toEpochDay()) }
    val selectedDate = remember(selectedDateEpochDay) { LocalDate.ofEpochDay(selectedDateEpochDay) }
    var screenMode by rememberSaveable { mutableStateOf(MainScreenMode.Normal) }
    var selectedGoal by rememberSaveable { mutableStateOf("Balanced") }
    var appLanguage by rememberSaveable { mutableStateOf(AppLanguage.English) }



    var syncOnWifiOnly by rememberSaveable { mutableStateOf(true) }
    var autoArchiveEnabled by rememberSaveable { mutableStateOf(false) }
    var tasks by remember {

        mutableStateOf(
            listOf(
                TimeTask(
                    id = "day-deep-work",
                    title = "Deep Work Block",
                    date = today,
                    startHour = 10,
                    startMinute = 0,
                    endHour = 14,
                    endMinute = 0,
                    color = ClockPurple,
                ),
                TimeTask(
                    id = "evening-study",
                    title = "Evening Study Sprint",
                    date = today.plusDays(1),
                    startHour = 16,
                    startMinute = 0,
                    endHour = 19,
                    endMinute = 0,
                    color = ClockBlue,
                ),
                TimeTask(
                    id = "night-reset",
                    title = "Night Reset Session",
                    date = today.minusDays(1),
                    startHour = 4,
                    startMinute = 0,
                    endHour = 9,
                    endMinute = 0,
                    color = ClockGreen,
                ),
            ),
        )
    }
    var activeBottomPage by rememberSaveable { mutableStateOf(BottomNavPage.Clock) }




    var draftTitle by rememberSaveable { mutableStateOf("") }
    var draftColorArgb by rememberSaveable { mutableIntStateOf(taskColorPalette.first().toArgb()) }
    var draftStartHour by rememberSaveable { mutableStateOf(8) }

    var draftStartMinute by rememberSaveable { mutableStateOf(0) }
    var draftEndHour by rememberSaveable { mutableStateOf(9) }
    var draftEndMinute by rememberSaveable { mutableStateOf(0) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var taskPendingDelete by remember { mutableStateOf<TimeTask?>(null) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var taskPendingEdit by remember { mutableStateOf<TimeTask?>(null) }
    var selectedClockTaskId by remember { mutableStateOf<String?>(null) }
    var pinnedClockTaskId by remember { mutableStateOf<String?>(null) }
    var selectedClockEmptySlot by remember { mutableStateOf<ClockEmptySlot?>(null) }


    var selectedClockTaskBounceKey by remember { mutableIntStateOf(0) }
    var selectedClockEmptySlotBounceKey by remember { mutableIntStateOf(0) }
    var showSetActionsExpanded by rememberSaveable { mutableStateOf(false) }



    var editingTaskId by rememberSaveable { mutableStateOf<String?>(null) }

    val sortedTasks = remember(tasks) {
        tasks.sortedWith(compareBy({ it.date }, { it.startTotalMinutes() }))
    }

    val selectedDateTasks = remember(sortedTasks, selectedDate) {
        sortedTasks.filter { it.date == selectedDate }
    }
    val tasksExcludingEditing = remember(selectedDateTasks, editingTaskId) {
        selectedDateTasks.filterNot { it.id == editingTaskId }
    }




    val draftStartTotalMinutes = draftStartHour * 60 + draftStartMinute

    val draftEndTotalMinutes = draftEndHour * 60 + draftEndMinute

    val earliestDraftEndTotalMinutes = (draftStartTotalMinutes + 1).coerceAtMost(23 * 60 + 59)
    val draftOverlapTask = remember(tasksExcludingEditing, draftStartTotalMinutes, draftEndTotalMinutes) {
        tasksExcludingEditing.firstOrNull {
            rangesOverlap(
                startA = draftStartTotalMinutes,
                endA = draftEndTotalMinutes,
                startB = it.startTotalMinutes(),
                endB = it.endTotalMinutes(),
            )
        }
    }
    val draftTimeError = when {
        draftEndTotalMinutes <= draftStartTotalMinutes -> "End time must be at least 1 minute after start time."
        draftOverlapTask != null -> "This time overlaps with ${draftOverlapTask.title} (${draftOverlapTask.prettyTimeRange()})."
        else -> null
    }


    val scope = rememberCoroutineScope()

    val shouldRunMinuteTicker = screenMode == MainScreenMode.Normal
    val now = rememberMinuteClockTime(isActive = shouldRunMinuteTicker)



    val todayTaskStats = remember(sortedTasks, today, now) {
        buildDayTaskStats(sortedTasks.filter { it.date == today }, now)
    }

    // — Foreground notification for active task timer —
    val activeTodayTask = remember(sortedTasks, today, now) {
        sortedTasks.filter { it.date == today }.firstOrNull { isTaskActive(it, now) }
    }
    LaunchedEffect(activeTodayTask, now) {
        if (activeTodayTask != null) {
            val remaining = activeTodayTask.normalizedEndTotalMinutes() - (now.hour * 60 + now.minute)
            TimerForegroundService.start(context, activeTodayTask.title, remaining.coerceAtLeast(0))
        } else {
            TimerForegroundService.stop(context)
        }
    }


    LaunchedEffect(taskStorage) {
        val storedTasks = taskStorage.getString("tasks_json", null)?.let(::decodeStoredTasks).orEmpty()
        if (storedTasks.isNotEmpty()) {
            tasks = storedTasks.sortedWith(compareBy({ it.date }, { it.startTotalMinutes() }))

        }
    }

    LaunchedEffect(tasks) {
        taskStorage.edit()
            .putString("tasks_json", encodeStoredTasks(tasks))
            .apply()
    }


    val currentGoalOption = remember(selectedGoal) {
        mineGoalOptions().firstOrNull { it.label == selectedGoal }
            ?: mineGoalOptions()[1]
    }

    val previewTasks = remember(
        selectedDateTasks,
        screenMode,
        editingTaskId,
        draftTitle,
        draftColorArgb,
        draftStartHour,
        draftStartMinute,
        draftEndHour,
        draftEndMinute,
        draftTimeError,
        selectedDate,
    ) {

        if (screenMode != MainScreenMode.SetProject || draftTimeError != null) {
            selectedDateTasks
        } else {
            val draftColor = Color(draftColorArgb)
            val draftPreviewTask = TimeTask(
                id = editingTaskId ?: "draft-preview",
                title = draftTitle.ifBlank {
                    if (editingTaskId != null) "Edit project" else "New project"
                },
                date = selectedDate,
                startHour = draftStartHour,
                startMinute = draftStartMinute,
                endHour = draftEndHour,
                endMinute = draftEndMinute,
                color = draftColor,
            )
            (tasksExcludingEditing + draftPreviewTask).sortedBy { it.startTotalMinutes() }
        }
    }









    val pagerState = rememberPagerState(pageCount = { 2 })
    val supportQuote = remember { supportQuotes.random() }
    val (supportQuoteBody, supportQuoteAuthor) = remember(supportQuote) {
        splitSupportQuote(supportQuote)
    }








    val isWarmupPhase = startupWarmupActive && screenMode == MainScreenMode.Normal
    var warmupStage by remember { mutableIntStateOf(0) }
    val useSimplifiedNormalPreview = isWarmupPhase || warmupStage < 2
    val enableFullPagerMotion = !startupWarmupActive && warmupStage >= 2
    val shouldComposeTimelinePage = warmupStage >= 1 && (!startupWarmupActive || pagerState.currentPage > 0 || pagerState.targetPage > 0)

    val shouldPrecomposeHiddenWarmup = startupWarmupActive || warmupStage < 3
    val shouldPrecomposeClockFace = startupWarmupActive || warmupStage >= 1
    val shouldPrecomposeTimelineWarmup = startupWarmupActive || warmupStage >= 1
    val shouldPrecomposeModeControls = startupWarmupActive || warmupStage >= 2
    val shouldPrecomposeTimePickerWarmup = startupWarmupActive || warmupStage >= 3

    LaunchedEffect(startupWarmupActive, screenMode) {
        if (!startupWarmupActive || screenMode != MainScreenMode.Normal) {
            warmupStage = 3
            return@LaunchedEffect
        }
        warmupStage = 0
        withFrameNanos { }
        warmupStage = 1
        withFrameNanos { }
        warmupStage = 2
        withFrameNanos { }
        warmupStage = 3
    }








    fun clearClockTaskPeakState(clearSelection: Boolean = false) {
        pinnedClockTaskId = null
        if (clearSelection) {
            selectedClockTaskId = null
        }
    }


    fun releaseClockTaskPeak() {
        if (pinnedClockTaskId != null) {
            return
        }
    }






    fun beginEditing(task: TimeTask) {
        selectedDateEpochDay = task.date.toEpochDay()
        activeBottomPage = BottomNavPage.Clock
        selectedClockTaskId = task.id
        pinnedClockTaskId = task.id
        selectedClockEmptySlot = null
        showSetActionsExpanded = false

        editingTaskId = task.id
        draftTitle = task.title
        draftColorArgb = task.color.toArgb()
        draftStartHour = task.startHour
        draftStartMinute = task.startMinute
        draftEndHour = task.endHour
        draftEndMinute = task.endMinute
        taskPendingEdit = null
        showEditDialog = false
        screenMode = MainScreenMode.SetProject

    }


    fun pinClockTaskPeak(task: TimeTask) {
        pinnedClockTaskId = task.id
        selectedClockTaskId = task.id
        selectedClockEmptySlot = null
        selectedClockTaskBounceKey += 1
        beginEditing(task)
    }



    fun resetDraft() {
        editingTaskId = null
        selectedClockEmptySlot = null
        draftTitle = suggestedDraftTitle(selectedGoal, isNightMode)
        draftColorArgb = preferredDraftColor(selectedGoal).toArgb()
        val defaultDraftRange = defaultDraftRangeForGoal(selectedGoal, isNightMode, syncOnWifiOnly)
        draftStartHour = defaultDraftRange.first / 60
        draftStartMinute = defaultDraftRange.first % 60
        draftEndHour = defaultDraftRange.second / 60
        draftEndMinute = defaultDraftRange.second % 60
    }




    fun returnToClockHome() {
        showSetActionsExpanded = false
        showEditDialog = false
        taskPendingEdit = null

        taskPendingDelete = null
        showDeleteDialog = false
        clearClockTaskPeakState(clearSelection = true)
        selectedClockEmptySlot = null
        screenMode = MainScreenMode.Normal
        resetDraft()
    }

    val canHandleBack = showDeleteDialog || showEditDialog || showSetActionsExpanded || screenMode == MainScreenMode.SetProject









    BackHandler(enabled = canHandleBack) {
        when {
            showDeleteDialog -> {
                showDeleteDialog = false
                taskPendingDelete = null
            }

            showEditDialog -> {
                showEditDialog = false
                taskPendingEdit = null
            }

            showSetActionsExpanded -> {
                showSetActionsExpanded = false
            }

            screenMode == MainScreenMode.SetProject -> {
                returnToClockHome()
            }


        }
    }







    fun applyDraftRange(startTotalMinutes: Int, endTotalMinutes: Int) {
        var normalizedStart = ((startTotalMinutes % (24 * 60)) + (24 * 60)) % (24 * 60)
        var normalizedEnd = ((endTotalMinutes % (24 * 60)) + (24 * 60)) % (24 * 60)
        if (endTotalMinutes > startTotalMinutes && normalizedEnd <= normalizedStart) {
            val maxStart = 23 * 60 + 58
            normalizedStart = normalizedStart.coerceAtMost(maxStart)
            normalizedEnd = (normalizedStart + 1).coerceAtMost(23 * 60 + 59)
        }
        draftStartHour = normalizedStart / 60
        draftStartMinute = normalizedStart % 60
        draftEndHour = normalizedEnd / 60
        draftEndMinute = normalizedEnd % 60
    }


    fun beginCreatingFromEmptySlot(slot: ClockEmptySlot) {
        clearClockTaskPeakState(clearSelection = true)
        selectedClockEmptySlot = slot
        showSetActionsExpanded = false
        editingTaskId = null
        draftTitle = suggestedDraftTitle(selectedGoal, isNightMode)
        draftColorArgb = nextAvailableTaskColor(tasksExcludingEditing, preferredDraftColor(selectedGoal)).toArgb()
        val boundedStart = slot.startTotalMinutes.coerceIn(0, 30 * 60 - 1)
        val boundedEnd = slot.endTotalMinutes.coerceIn(boundedStart + 1, 30 * 60)
        applyDraftRange(
            startTotalMinutes = boundedStart,
            endTotalMinutes = boundedEnd,
        )
        taskPendingEdit = null

        showEditDialog = false
        screenMode = MainScreenMode.SetProject
    }





    LaunchedEffect(selectedClockEmptySlot, sortedTasks) {
        val emptySlot = selectedClockEmptySlot ?: return@LaunchedEffect
        val activeWindowStart = if (isNightMode) 18 * 60 else 6 * 60
        val activeWindowEnd = if (isNightMode) 30 * 60 else 18 * 60
        if (
            emptySlot.startTotalMinutes < activeWindowStart ||
            emptySlot.endTotalMinutes > activeWindowEnd ||
            tasksForMode(sortedTasks, isNightMode).any { task ->
                val taskStart = task.startTotalMinutes()
                val taskEnd = task.normalizedEndTotalMinutes()
                val overlapStart = max(emptySlot.startTotalMinutes, taskStart)
                val overlapEnd = kotlin.math.min(emptySlot.endTotalMinutes, taskEnd)
                overlapEnd > overlapStart
            }
        ) {
            selectedClockEmptySlot = null
        }
    }


    fun updateDraftStart(totalMinutes: Int) {
        val bounded = totalMinutes.coerceIn(0, 23 * 60 + 58)
        draftStartHour = bounded / 60
        draftStartMinute = bounded % 60
        if (draftEndTotalMinutes <= bounded) {
            val nextMinute = (bounded + 1).coerceAtMost(23 * 60 + 59)
            draftEndHour = nextMinute / 60
            draftEndMinute = nextMinute % 60
        }
    }

    fun updateDraftEnd(totalMinutes: Int) {
        val bounded = totalMinutes.coerceIn(earliestDraftEndTotalMinutes, 23 * 60 + 59)
        draftEndHour = bounded / 60
        draftEndMinute = bounded % 60
    }

    fun saveDraft() {

        if (draftTimeError != null) return
        val draftColor = Color(draftColorArgb)
        val title = draftTitle.ifBlank { "New project" }
        val savedTask = TimeTask(
            id = editingTaskId ?: "task-${System.currentTimeMillis()}",
            title = title,
            date = selectedDate,
            startHour = draftStartHour,
            startMinute = draftStartMinute,
            endHour = draftEndHour,
            endMinute = draftEndMinute,
            color = draftColor,
        )
        tasks = (tasks.filterNot { it.id == editingTaskId } + savedTask)
            .sortedWith(compareBy({ it.date }, { it.startTotalMinutes() }))

        selectedClockTaskId = savedTask.id
        pinnedClockTaskId = savedTask.id
        selectedClockTaskBounceKey += 1

        selectedClockEmptySlot = null
        screenMode = MainScreenMode.Normal
        activeBottomPage = BottomNavPage.Clock
        resetDraft()
    }






    val primaryActionContainerColor = neutralFilledButtonContainerColor()
    val primaryActionContentColor = neutralFilledButtonContentColor()
    val density = LocalDensity.current
    val coinFlipCommitAngle = 52f
    val coinFlipHalfTurnAngle = 104f
    val coinFlipMaxAngle = 176f
    val coinFlipOvershootAngle = 10f
    val coinFlipReturnDurationMillis = 220
    val coinFlipHalfTurnDurationMillis = 160
    val coinFlipOvershootDurationMillis = 120
    val coinFlipReboundDurationMillis = 140

    var clockTransitionStyle by remember { mutableStateOf(com.vtm.app.components.ClockModeTransitionStyle.None) }
    var clockPulseTriggerKey by remember { mutableIntStateOf(0) }
    var coinFlipDragRotation by remember { mutableFloatStateOf(0f) }
    var coinFlipBaseNightMode by remember { mutableStateOf(isNightMode) }
    var coinFlipCommittedTargetNightMode by remember { mutableStateOf<Boolean?>(null) }
    var coinFlipDirection by remember { mutableFloatStateOf(1f) }
    var isCoinFlipSettling by remember { mutableStateOf(false) }
    var isCoinFlipInteractionActive by remember { mutableStateOf(false) }
    val coinFlipRotation = remember { Animatable(0f) }
    val modeDragScope = rememberCoroutineScope()
    val shouldEnableCoinFlip = false

    val isClockFlipInFlight = isCoinFlipSettling || isCoinFlipInteractionActive

    LaunchedEffect(isNightMode, isCoinFlipSettling) {

        if (!isCoinFlipSettling && kotlin.math.abs(coinFlipDragRotation) <= 0.5f) {
            coinFlipBaseNightMode = isNightMode
        }
    }



    @Composable
    fun CoinFlipClock(
        modifier: Modifier = Modifier,
        displayNightMode: Boolean,
    ) {
        BoxWithConstraints(
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(shouldEnableCoinFlip, isCoinFlipSettling, isNightMode) {
                    if (!shouldEnableCoinFlip || isCoinFlipSettling) return@pointerInput
                    val dragWidth = size.width.toFloat().coerceAtLeast(1f)
                    detectHorizontalDragGestures(
                        onDragStart = {
                            clockTransitionStyle = ClockModeTransitionStyle.None
                            coinFlipBaseNightMode = isNightMode
                            coinFlipCommittedTargetNightMode = null
                            coinFlipDragRotation = 0f
                            isCoinFlipInteractionActive = true
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            val deltaRotation = (dragAmount / dragWidth) * 180f
                            val nextRotation = (coinFlipDragRotation + deltaRotation)
                                .coerceIn(-coinFlipMaxAngle, coinFlipMaxAngle)
                            coinFlipDragRotation = nextRotation
                            if (kotlin.math.abs(nextRotation) > 1f) {
                                coinFlipDirection = if (nextRotation >= 0f) 1f else -1f
                            }
                            change.consume()
                        },
                        onDragEnd = {
                            val finalRotation = coinFlipDragRotation
                            val shouldCommit = kotlin.math.abs(finalRotation) >= coinFlipCommitAngle
                            val targetDirection = when {
                                kotlin.math.abs(finalRotation) > 0.5f -> if (finalRotation >= 0f) 1f else -1f
                                else -> coinFlipDirection
                            }
                            isCoinFlipSettling = true
                            modeDragScope.launch {
                                coinFlipRotation.snapTo(finalRotation)
                                if (shouldCommit) {
                                    val targetNightMode = !coinFlipBaseNightMode
                                    coinFlipCommittedTargetNightMode = targetNightMode
                                    coinFlipDirection = targetDirection
                                    coinFlipRotation.animateTo(
                                        targetValue = targetDirection * coinFlipHalfTurnAngle,
                                        animationSpec = tween(
                                            durationMillis = coinFlipHalfTurnDurationMillis,
                                            easing = FastOutSlowInEasing,
                                        ),
                                    )
                                    onNightModeChange(targetNightMode)
                                    coinFlipRotation.animateTo(



                                        targetValue = targetDirection * (180f + coinFlipOvershootAngle),
                                        animationSpec = tween(
                                            durationMillis = coinFlipOvershootDurationMillis,
                                            easing = LinearOutSlowInEasing,
                                        ),
                                    )
                                    coinFlipRotation.animateTo(
                                        targetValue = targetDirection * (180f - coinFlipOvershootAngle * 0.72f),
                                        animationSpec = tween(
                                            durationMillis = coinFlipReboundDurationMillis,
                                            easing = FastOutSlowInEasing,
                                        ),
                                    )
                                    coinFlipRotation.animateTo(
                                        targetValue = targetDirection * 180f,
                                        animationSpec = tween(
                                            durationMillis = coinFlipReboundDurationMillis,
                                            easing = FastOutSlowInEasing,
                                        ),
                                    )
                                    coinFlipBaseNightMode = targetNightMode
                                    clockTransitionStyle = ClockModeTransitionStyle.Pulse
                                    clockPulseTriggerKey += 1
                                } else {
                                    coinFlipRotation.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(
                                            durationMillis = coinFlipReturnDurationMillis,
                                            easing = FastOutSlowInEasing,
                                        ),
                                    )
                                }
                                coinFlipDragRotation = 0f
                                coinFlipCommittedTargetNightMode = null
                                coinFlipRotation.snapTo(0f)
                                isCoinFlipSettling = false
                                isCoinFlipInteractionActive = false
                            }
                        },
                        onDragCancel = {
                            coinFlipDragRotation = 0f
                            coinFlipCommittedTargetNightMode = null
                            isCoinFlipInteractionActive = false
                        },
                    )
                },
        ) {
            val cameraDistancePx = with(density) { 132.dp.toPx() }
            val coinRotation = if (isCoinFlipSettling) {
                coinFlipRotation.value
            } else {
                coinFlipDragRotation
            }
            val rotationRadians = Math.toRadians(coinRotation.toDouble())
            val absSin = kotlin.math.abs(kotlin.math.sin(rotationRadians)).toFloat()
            val absCos = kotlin.math.abs(kotlin.math.cos(rotationRadians)).toFloat()
            val faceVisibility = (absCos * absCos * 1.08f).coerceIn(0f, 1f)
            val frontAlpha = if (kotlin.math.abs(coinRotation) <= 90f) faceVisibility else 0f
            val backAlpha = if (kotlin.math.abs(coinRotation) >= 90f) faceVisibility else 0f
            val backFaceRotationY = if (coinRotation >= 0f) coinRotation - 180f else coinRotation + 180f
            val faceScaleX = 0.992f - 0.03f * (1f - absCos)
            val faceScaleY = 0.992f + 0.015f * absSin
            val faceLift = with(density) { absSin * 8.dp.toPx() }
            val frontNightMode = coinFlipBaseNightMode
            val backNightMode = !coinFlipBaseNightMode
            val rimStrokePx = with(density) { (8.dp + 14.dp * absSin).toPx() }
            val rimAlpha = (0.22f + 0.54f * absSin).coerceIn(0f, 0.9f)
            val rimInsetPx = rimStrokePx / 2f + with(density) { 6.dp.toPx() }
            val shadowWidthDp = with(density) { 104.dp + 18.dp * absSin }
            val shadowHeightDp = with(density) { 7.dp + 3.dp * absSin }

            val shadowAlpha = (0.07f + 0.08f * absSin).coerceAtMost(0.16f)
            val shadowAnchorYPx = with(density) { 92.dp.toPx() }
            val shadowScaleX = 0.92f + 0.06f * absSin
            val shadowScaleY = 0.76f + 0.04f * absSin
            val overlayAlphaMultiplier = if (isClockFlipInFlight) 1f else 0f
            val edgeHighlightAlpha = (0.16f + 0.26f * absSin).coerceAtMost(0.38f) * overlayAlphaMultiplier

            val frontShadeAlpha = (0.05f + 0.12f * absSin + 0.04f * ((coinRotation / 180f).coerceIn(-1f, 1f))).coerceIn(0f, 0.22f) * overlayAlphaMultiplier
            val backShadeAlpha = (0.08f + 0.16f * absSin).coerceIn(0f, 0.26f) * overlayAlphaMultiplier
            val colorTransitionProgress = when {
                coinRotation >= 0f -> (coinRotation / coinFlipHalfTurnAngle).coerceIn(0f, 1f)
                else -> 1f - ((-coinRotation) / coinFlipHalfTurnAngle).coerceIn(0f, 1f)
            }
            val shouldUseInteractiveColorTransition = isClockFlipInFlight || coinFlipCommittedTargetNightMode != null
            val frontFaceTransitionProgress = if (shouldUseInteractiveColorTransition) {
                colorTransitionProgress
            } else {
                null
            }
            val frontFaceTransitionStyle = if (shouldUseInteractiveColorTransition) {
                ClockModeTransitionStyle.Morph
            } else if (displayNightMode == isNightMode) {
                clockTransitionStyle
            } else {
                ClockModeTransitionStyle.None
            }
            val frontFacePulseTriggerKey = if (shouldUseInteractiveColorTransition) {
                0
            } else if (displayNightMode == isNightMode) {
                clockPulseTriggerKey
            } else {
                0
            }
            val frontFaceRenderQuality = if (isClockFlipInFlight) {
                ClockFaceRenderQuality.Reduced
            } else {
                ClockFaceRenderQuality.Full
            }
            val frontFaceNightMode = if (shouldUseInteractiveColorTransition) {
                colorTransitionProgress >= 0.5f
            } else {
                frontNightMode
            }

            val faceModifier = Modifier.fillMaxWidth().aspectRatio(1f)
            val staticRimAlpha = if (displayNightMode) 0.12f else 0.07f
            val rimBrightColor = Color.White.copy(alpha = edgeHighlightAlpha * 0.88f)
            val rimSurfaceColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.98f)
            val rimDarkColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            val rimStaticColor = MaterialTheme.colorScheme.onSurface.copy(alpha = staticRimAlpha)
            val rimOutlineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = if (isClockFlipInFlight) 0.06f + edgeHighlightAlpha * 0.24f else staticRimAlpha * 0.7f)
            val rimOutlineStrokePx = with(density) { 1.2.dp.toPx() }





            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .width(shadowWidthDp)
                        .height(shadowHeightDp)
                        .graphicsLayer {
                            alpha = shadowAlpha
                            translationY = shadowAnchorYPx - faceLift * 0.18f
                            scaleX = shadowScaleX
                            scaleY = shadowScaleY
                        }
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.14f),
                                    Color.Black.copy(alpha = 0.08f),
                                    Color.Transparent,
                                ),
                            ),
                            shape = RoundedCornerShape(percent = 50),
                        ),
                )


                Canvas(
                    modifier = faceModifier
                        .graphicsLayer {
                            rotationY = coinRotation
                            cameraDistance = cameraDistancePx
                            alpha = rimAlpha
                            translationY = -faceLift
                            scaleX = faceScaleX
                            scaleY = faceScaleY
                        },
                ) {

                    val diameter = size.minDimension - rimInsetPx * 2f
                    if (diameter > 0f) {
                        if (isClockFlipInFlight) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        rimBrightColor,
                                        rimSurfaceColor,
                                        rimDarkColor,
                                        rimSurfaceColor,
                                        rimBrightColor,
                                    ),
                                    center = center,
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = Offset(rimInsetPx, rimInsetPx),
                                size = Size(diameter, diameter),
                                style = Stroke(width = rimStrokePx, cap = StrokeCap.Round),
                            )
                        } else {
                            drawArc(
                                color = rimStaticColor,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = Offset(rimInsetPx, rimInsetPx),
                                size = Size(diameter, diameter),
                                style = Stroke(width = rimStrokePx, cap = StrokeCap.Round),
                            )
                        }
                        drawArc(
                            color = rimOutlineColor,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = Offset(rimInsetPx, rimInsetPx),
                            size = Size(diameter, diameter),
                            style = Stroke(width = rimOutlineStrokePx, cap = StrokeCap.Round),
                        )
                    }

                }

                Box(
                    modifier = faceModifier
                        .graphicsLayer {
                            rotationY = backFaceRotationY
                            cameraDistance = cameraDistancePx
                            alpha = backAlpha
                            scaleX = faceScaleX
                            scaleY = faceScaleY
                            translationY = -faceLift
                        },
                ) {
                    ClockFace(
                        isNightMode = backNightMode,
                        tasks = previewTasks,
                        currentTime = now,
                        modifier = Modifier.fillMaxSize(),
                        modeTransitionStyle = ClockModeTransitionStyle.None,
                        pulseTriggerKey = 0,
                        renderQuality = ClockFaceRenderQuality.Reduced,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = backShadeAlpha * 0.16f),
                                        Color.Black.copy(alpha = backShadeAlpha),
                                        Color.Transparent,
                                    ),
                                ),
                                shape = CircleShape,
                            ),
                    )
                }

                Box(
                    modifier = faceModifier
                        .graphicsLayer {
                            rotationY = coinRotation
                            cameraDistance = cameraDistancePx
                            alpha = frontAlpha
                            scaleX = faceScaleX
                            scaleY = faceScaleY
                            translationY = -faceLift
                        },
                ) {
                    ClockFace(
                        isNightMode = frontFaceNightMode,
                        tasks = previewTasks,
                        currentTime = now,
                        modifier = Modifier.fillMaxSize(),
                        modeTransitionStyle = frontFaceTransitionStyle,
                        pulseTriggerKey = frontFacePulseTriggerKey,
                        renderQuality = frontFaceRenderQuality,
                        externalTransitionProgress = frontFaceTransitionProgress,
                        selectedTaskId = selectedClockTaskId,
                        pinnedPeakTaskId = pinnedClockTaskId,

                        selectedEmptySlot = selectedClockEmptySlot,
                        selectedTaskBounceKey = selectedClockTaskBounceKey,
                        selectedEmptySlotBounceKey = selectedClockEmptySlotBounceKey,
                        onTaskPress = { pressedTask ->
                            selectedClockEmptySlot = null
                            if (screenMode == MainScreenMode.SetProject) {
                                beginEditing(pressedTask)
                            } else {
                                selectedClockTaskId = null
                                pinnedClockTaskId = null
                            }
                        },

                        onTaskRelease = {
                            releaseClockTaskPeak()
                        },

                        onTaskLongPress = { longPressedTask ->
                            pinClockTaskPeak(longPressedTask)
                        },
                        onTaskSingleTap = { tappedTask ->
                            if (screenMode == MainScreenMode.SetProject) {
                                beginEditing(tappedTask)
                            } else {
                                selectedClockEmptySlot = null
                                pinnedClockTaskId = null
                                selectedClockTaskId = null
                            }

                        },


                        onTaskDoubleTap = { tappedTask ->
                            selectedClockEmptySlot = null
                            selectedClockTaskId = tappedTask.id
                            pinnedClockTaskId = tappedTask.id
                            selectedClockTaskBounceKey += 1
                            beginEditing(tappedTask)
                        },

                        onEmptyPress = { pressedEmptySlot ->
                            clearClockTaskPeakState(clearSelection = true)
                            selectedClockEmptySlot = pressedEmptySlot
                            selectedClockEmptySlotBounceKey += 1
                        },
                        onEmptyRelease = { releasedEmptySlot ->
                            if (selectedClockEmptySlot == releasedEmptySlot) {
                                scope.launch {
                                    delay(220)
                                    if (selectedClockEmptySlot == releasedEmptySlot) {
                                        selectedClockEmptySlot = null
                                    }
                                }
                            }
                        },
                        onEmptySingleTap = { tappedEmptySlot ->
                            if (selectedClockEmptySlot != tappedEmptySlot) {
                                selectedClockEmptySlot = tappedEmptySlot
                            }
                            scope.launch {
                                delay(220)
                                if (selectedClockEmptySlot == tappedEmptySlot) {
                                    selectedClockEmptySlot = null
                                }
                            }
                        },



                        onEmptyDoubleTap = { tappedEmptySlot ->
                            clearClockTaskPeakState(clearSelection = true)
                            selectedClockEmptySlot = tappedEmptySlot
                            selectedClockEmptySlotBounceKey += 1
                            beginCreatingFromEmptySlot(tappedEmptySlot)
                        },
                    )




                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = edgeHighlightAlpha * 0.8f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = frontShadeAlpha),
                                    ),
                                ),
                                shape = CircleShape,
                            ),
                    )
                }
            }

        }
    }




    @Composable
    fun ScreenScaffold(
        displayNightMode: Boolean,
        modifier: Modifier = Modifier,
        useSimplifiedNormalPreview: Boolean = false,
    ) {
        VTMTheme(
            darkTheme = displayNightMode,
            applySystemBars = false,
        ) {
            val pageVisibleTasks = remember(selectedDateTasks, displayNightMode) {
                tasksForMode(selectedDateTasks, displayNightMode)
            }

            val pageCurrentTask = remember(pageVisibleTasks, now) {
                pageVisibleTasks.firstOrNull { task -> isTaskActive(task, now) }
            }
            val pageNextTask = remember(pageVisibleTasks, pageCurrentTask, now) {
                when {
                    pageVisibleTasks.isEmpty() -> null
                    pageCurrentTask != null -> {
                        val currentIndex = pageVisibleTasks.indexOfFirst { it.id == pageCurrentTask.id }
                        if (currentIndex >= 0 && currentIndex < pageVisibleTasks.lastIndex) {
                            pageVisibleTasks[currentIndex + 1]
                        } else {
                            pageVisibleTasks.firstOrNull { it.id != pageCurrentTask.id }
                        }
                    }
                    else -> pageVisibleTasks.minByOrNull { task -> minutesUntilTask(task, now) }
                }
            }
            val configuration = LocalConfiguration.current

            val isCompactWidth = configuration.screenWidthDp < 380
            val contentHorizontalPadding = if (isCompactWidth) 16.dp else 24.dp
            val contentVerticalPadding = if (isCompactWidth) 16.dp else 20.dp
            val primaryActionHorizontalPadding = if (isCompactWidth) 16.dp else 24.dp

            val shouldShowBottomPrimaryAction = screenMode == MainScreenMode.SetProject
            val primaryActionLabel = if (editingTaskId != null) "Back to Home" else "Back"
            val shouldShowBottomNav = screenMode == MainScreenMode.Normal


            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    when {
                        shouldShowBottomPrimaryAction -> {
                            Button(
                                onClick = {
                                    returnToClockHome()
                                },

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = primaryActionHorizontalPadding, vertical = 16.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                    containerColor = primaryActionContainerColor,
                                    contentColor = primaryActionContentColor,
                                ),
                            ) {
                                Text(
                                    text = primaryActionLabel,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }

                        shouldShowBottomNav -> {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                                tonalElevation = 0.dp,
                            ) {
                                bottomNavItems.forEach { item ->
                                    val selected = activeBottomPage == item.page
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            activeBottomPage = item.page
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.label,
                                            )
                                        },
                                        label = null,
                                        alwaysShowLabel = false,
                                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.88f),
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                                        ),
                                    )
                                }
                            }
                        }
                    }
                },
            ) { paddingValues ->

                // — Three-page HorizontalPager for swipe navigation —
                val mainPagerState = rememberPagerState(
                    initialPage = BottomNavPage.Clock.index,
                    pageCount = { 3 },
                )

                // Sync pager → bottom nav
                LaunchedEffect(mainPagerState.currentPage, mainPagerState.targetPage) {
                    val page = if (mainPagerState.isScrollInProgress) mainPagerState.targetPage else mainPagerState.currentPage
                    val synced = BottomNavPage.fromIndex(page)
                    if (activeBottomPage != synced && screenMode == MainScreenMode.Normal) {
                        activeBottomPage = synced
                    }
                }
                // Sync bottom nav → pager
                LaunchedEffect(activeBottomPage) {
                    if (screenMode == MainScreenMode.Normal && mainPagerState.currentPage != activeBottomPage.index) {
                        mainPagerState.animateScrollToPage(activeBottomPage.index)
                    }
                }

                HorizontalPager(
                    state = mainPagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    beyondViewportPageCount = 1,
                    userScrollEnabled = screenMode == MainScreenMode.Normal,
                ) { page ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = contentHorizontalPadding, vertical = contentVerticalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (screenMode == MainScreenMode.Normal && page == BottomNavPage.Clock.index) {
                        // — Date navigation strip —
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextButton(
                                onClick = { selectedDateEpochDay = selectedDate.minusDays(1).toEpochDay() },
                                contentPadding = PaddingValues(horizontal = 4.dp),
                            ) { Text("‹", fontSize = 16.sp) }
                            Text(
                                text = "${selectedDate.monthValue}/${selectedDate.dayOfMonth} ${appLanguage.strWeekday(selectedDate.dayOfWeek.value)}",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            TextButton(
                                onClick = { selectedDateEpochDay = selectedDate.plusDays(1).toEpochDay() },
                                contentPadding = PaddingValues(horizontal = 4.dp),
                            ) { Text("›", fontSize = 16.sp) }
                            Spacer(modifier = Modifier.width(12.dp))
                            TextButton(
                                onClick = {
                                    activeBottomPage = BottomNavPage.Calendar
                                },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            ) { Text(appLanguage.strViewCalendar(), fontSize = 11.sp) }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = supportQuoteBody,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.Serif,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            supportQuoteAuthor?.let { author ->
                                Text(
                                    text = "— $author",
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 11.sp,
                                    lineHeight = 13.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    textAlign = TextAlign.End,
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    } else if (screenMode != MainScreenMode.Normal && page == BottomNavPage.Clock.index) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    if (screenMode == MainScreenMode.Normal && page == BottomNavPage.Calendar.index) {
                        CalendarReservedPage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = true),
                            selectedDate = selectedDate,
                            tasks = sortedTasks,
                            onSelectedDateChange = { selectedDateEpochDay = it.toEpochDay() },
                            onOpenClockDay = {
                                activeBottomPage = BottomNavPage.Clock
                                screenMode = MainScreenMode.Normal
                            },
                            onTaskClick = { task ->
                                selectedDateEpochDay = task.date.toEpochDay()
                                selectedClockTaskId = task.id
                                pinnedClockTaskId = task.id
                                selectedClockEmptySlot = null
                                activeBottomPage = BottomNavPage.Clock
                                screenMode = MainScreenMode.Normal
                                selectedClockTaskBounceKey += 1
                            },
                            onTaskDelete = { task ->
                                tasks = tasks.filterNot { it.id == task.id }
                            },
                            onTaskEdit = { task ->
                                beginEditing(task)
                            },
                            onAddTask = {
                                resetDraft()
                                screenMode = MainScreenMode.SetProject
                                activeBottomPage = BottomNavPage.Clock
                            },
                            appLanguage = appLanguage,
                        )
                    } else if (screenMode == MainScreenMode.Normal && page == BottomNavPage.Profile.index) {
                        MineReservedPage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = true),
                            selectedGoal = selectedGoal,
                            syncOnWifiOnly = syncOnWifiOnly,
                            autoArchiveEnabled = autoArchiveEnabled,
                            todayTaskStats = todayTaskStats,
                            currentGoalAccent = currentGoalOption.accent,
                            isNightMode = displayNightMode,
                            appLanguage = appLanguage,
                            onGoalChange = { selectedGoal = it },
                            onSyncChange = { syncOnWifiOnly = it },
                            onAutoArchiveChange = { autoArchiveEnabled = it },
                            onNightModeChange = onNightModeChange,
                            onLanguageChange = { appLanguage = it },
                            onNavigateToClock = {
                                activeBottomPage = BottomNavPage.Clock
                                screenMode = MainScreenMode.Normal
                            },
                            onNavigateToCalendar = {
                                activeBottomPage = BottomNavPage.Calendar
                                screenMode = MainScreenMode.Normal
                            },
                        )
                    } else if (page == BottomNavPage.Clock.index) {

                        if (useSimplifiedNormalPreview && screenMode == MainScreenMode.Normal) {
                            if (pageCurrentTask == null && pageNextTask == null) {
                                // — Empty state on Clock page —
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = appLanguage.strNoTasksEmpty(),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = appLanguage.strAddFirstTask(),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        fontSize = 13.sp,
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    ProjectSummaryCard(
                                        sectionTitle = "Current",
                                        task = pageCurrentTask,
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    ProjectSummaryCard(
                                        sectionTitle = "Next",
                                        task = pageNextTask,
                                    )
                                }
                            }
                        } else {
                            CoinFlipClock(
                                modifier = Modifier.fillMaxWidth(),
                                displayNightMode = displayNightMode,
                            )
                        }








                        if (screenMode != MainScreenMode.SetProject) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                ModeToggleRow(
                                    isNightMode = displayNightMode,
                                    onNightModeChange = { targetNightMode ->
                                        if (targetNightMode == isNightMode) return@ModeToggleRow
                                        clockTransitionStyle = ClockModeTransitionStyle.Morph
                                        clockPulseTriggerKey += 1
                                        onNightModeChange(targetNightMode)
                                    },
                                )

                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    if (!useSimplifiedNormalPreview && screenMode == MainScreenMode.Normal) {
                                        Box {
                                            if (showSetActionsExpanded) {
                                                TopSetActionsCluster(
                                                    hasTasks = tasks.isNotEmpty(),
                                                    onNewProject = {
                                                        resetDraft()
                                                        screenMode = MainScreenMode.SetProject
                                                    },
                                                    onEditProject = {
                                                        taskPendingEdit = null
                                                        showEditDialog = true
                                                    },
                                                    onDeleteProject = {
                                                        taskPendingDelete = null
                                                        showDeleteDialog = true
                                                    },
                                                )
                                            } else {
                                                SetTriggerButton(
                                                    onClick = { showSetActionsExpanded = true },
                                                    containerColor = primaryActionContainerColor,
                                                    contentColor = primaryActionContentColor,
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        PagerIndicatorRow(
                                            currentPage = pagerState.currentPage,
                                            pageCount = 2,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                        }




                        if (useSimplifiedNormalPreview && screenMode == MainScreenMode.Normal) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f, fill = true),
                                verticalArrangement = Arrangement.Top,
                            ) {
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        } else {
                            AnimatedContent(
                                targetState = screenMode,
                                transitionSpec = {
                                    val movingForward = targetState.transitionLevel() > initialState.transitionLevel()
                                    val duration = 320
                                    (slideInHorizontally(
                                        animationSpec = tween(durationMillis = duration),
                                        initialOffsetX = { fullWidth -> if (movingForward) fullWidth / 5 else -fullWidth / 5 },
                                    ) + fadeIn(animationSpec = tween(durationMillis = duration))) togetherWith
                                        (slideOutHorizontally(
                                            animationSpec = tween(durationMillis = duration),
                                            targetOffsetX = { fullWidth -> if (movingForward) -fullWidth / 6 else fullWidth / 6 },
                                        ) + fadeOut(animationSpec = tween(durationMillis = duration))) using
                                        SizeTransform(clip = false)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f, fill = true),
                                label = "screen-mode-content",
                            ) { animatedScreenMode ->
                                when (animatedScreenMode) {
                                    MainScreenMode.Normal -> {
                                        HorizontalPager(
                                            state = pagerState,
                                            modifier = Modifier.fillMaxWidth(),
                                            beyondViewportPageCount = 1,
                                            userScrollEnabled = pageVisibleTasks.isNotEmpty() && !isClockFlipInFlight,
                                            verticalAlignment = Alignment.Top,
                                        ) { page ->
                                            val pageOffset = kotlin.math.abs(
                                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction,
                                            ).coerceIn(0f, 1f)
                                            val direction = if (page >= pagerState.currentPage) 1f else -1f
                                            val animatedTranslationX by animateFloatAsState(
                                                targetValue = if (enableFullPagerMotion) interpolateFloat(0f, 112f, pageOffset) else 0f,
                                                animationSpec = spring(
                                                    dampingRatio = 0.72f,
                                                    stiffness = 280f,
                                                ),
                                                label = "pager-translation-x",
                                            )
                                            val animatedScale by animateFloatAsState(
                                                targetValue = if (enableFullPagerMotion) interpolateFloat(1f, 0.88f, pageOffset) else 1f,
                                                animationSpec = spring(
                                                    dampingRatio = 0.74f,
                                                    stiffness = 250f,
                                                ),
                                                label = "pager-scale",
                                            )
                                            val animatedAlpha by animateFloatAsState(
                                                targetValue = if (enableFullPagerMotion) interpolateFloat(1f, 0.56f, pageOffset) else 1f,
                                                animationSpec = spring(
                                                    dampingRatio = 0.86f,
                                                    stiffness = 320f,
                                                ),
                                                label = "pager-alpha",
                                            )
                                            val animatedRotationY by animateFloatAsState(
                                                targetValue = if (enableFullPagerMotion) interpolateFloat(0f, 7.5f, pageOffset) * direction else 0f,
                                                animationSpec = spring(
                                                    dampingRatio = 0.78f,
                                                    stiffness = 260f,
                                                ),
                                                label = "pager-rotation-y",
                                            )
                                            val animatedScaleY by animateFloatAsState(
                                                targetValue = if (enableFullPagerMotion) interpolateFloat(1f, 0.94f, pageOffset) else 1f,
                                                animationSpec = spring(
                                                    dampingRatio = 0.8f,
                                                    stiffness = 240f,
                                                ),
                                                label = "pager-scale-y",
                                            )

                                            val pageMotionModifier = Modifier.graphicsLayer {
                                                translationX = animatedTranslationX * direction
                                                scaleX = animatedScale
                                                scaleY = animatedScaleY
                                                alpha = animatedAlpha
                                                rotationY = animatedRotationY
                                                cameraDistance = 18.dp.toPx()
                                            }



                                            when (page) {
                                                0 -> {
                                                    Column(
                                                        modifier = pageMotionModifier
                                                            .fillMaxWidth()
                                                            .fillMaxSize(),
                                                    ) {
                                                        if (pageCurrentTask == null && pageNextTask == null) {
                                                            Column(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(vertical = 20.dp),
                                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                            ) {
                                                                Text(
                                                                    text = appLanguage.strNoTasksEmpty(),
                                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                                                    fontSize = 15.sp,
                                                                    fontWeight = FontWeight.Medium,
                                                                )
                                                                Spacer(modifier = Modifier.height(6.dp))
                                                                Text(
                                                                    text = appLanguage.strAddFirstTask(),
                                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                                                    fontSize = 13.sp,
                                                                )
                                                            }
                                                        } else {
                                                            ProjectSummaryCard(
                                                                sectionTitle = "Current",
                                                                task = pageCurrentTask,
                                                            )

                                                            Spacer(modifier = Modifier.height(10.dp))

                                                            ProjectSummaryCard(
                                                                sectionTitle = "Next",
                                                                task = pageNextTask,
                                                            )
                                                        }





                                                    }
                                                }


                                                else -> {
                                                    Box(
                                                        modifier = pageMotionModifier
                                                            .fillMaxWidth()
                                                            .fillMaxSize(),
                                                        contentAlignment = Alignment.TopStart,
                                                    ) {
                                                        if (shouldComposeTimelinePage) {
                                                            TimelinePreview(
                                                                tasks = sortedTasks,
                                                                modifier = Modifier.fillMaxSize(),
                                                            )
                                                        } else {
                                                            Column(
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .padding(top = 6.dp),
                                                                verticalArrangement = Arrangement.Top,
                                                            ) {
                                                                OutlinedCard(
                                                                    modifier = Modifier.fillMaxWidth(),
                                                                    shape = RoundedCornerShape(24.dp),
                                                                    colors = CardDefaults.outlinedCardColors(
                                                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f),
                                                                    ),
                                                                    border = BorderStroke(
                                                                        width = 1.dp,
                                                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f),
                                                                    ),
                                                                ) {
                                                                    Column(
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .padding(horizontal = 18.dp, vertical = 22.dp),
                                                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                                                    ) {
                                                                        Text(
                                                                            text = "Today timeline",
                                                                            style = MaterialTheme.typography.titleMedium,
                                                                            fontWeight = FontWeight.SemiBold,
                                                                        )
                                                                        Text(
                                                                            text = "Will finish preparing when you slide here for the first time.",
                                                                            style = MaterialTheme.typography.bodyMedium,
                                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }

                                    }


                                    MainScreenMode.SetProject -> {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .verticalScroll(rememberScrollState()),
                                        ) {
                                            DraftProjectCard(
                                                isEditing = editingTaskId != null,
                                                title = draftTitle,
                                                onTitleChange = { onTitleChange -> draftTitle = onTitleChange },
                                                selectedColor = Color(draftColorArgb),
                                                onSelectColor = {
                                                    draftColorArgb = it.toArgb()
                                                },
                                                startHour = draftStartHour,
                                                startMinute = draftStartMinute,
                                                endHour = draftEndHour,
                                                endMinute = draftEndMinute,
                                                earliestEndTotalMinutes = earliestDraftEndTotalMinutes,
                                                occupiedStartTimes = unavailableStartTimes(tasksExcludingEditing),
                                                occupiedEndTimes = unavailableEndTimes(tasksExcludingEditing, earliestDraftEndTotalMinutes),
                                                draftColor = Color(draftColorArgb),
                                                timeError = draftTimeError,
                                                onSelectStartTime = { updateDraftStart(it) },
                                                onSelectEndTime = { updateDraftEnd(it) },
                                                onSave = { saveDraft() },
                                                appLanguage = appLanguage,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                } // end Column
                } // end HorizontalPager page

        }
    }


    CompositionLocalProvider(LocalAppLanguage provides appLanguage) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScreenScaffold(
            displayNightMode = isNightMode,
            modifier = Modifier.fillMaxSize(),
            useSimplifiedNormalPreview = useSimplifiedNormalPreview,
        )

        if (shouldPrecomposeHiddenWarmup && screenMode == MainScreenMode.Normal) {
            HiddenWarmupPrecompose(
                shouldPrecomposeClockFace = shouldPrecomposeClockFace,
                shouldPrecomposeTimeline = shouldPrecomposeTimelineWarmup,
                shouldPrecomposeModeControls = shouldPrecomposeModeControls,
                shouldPrecomposeTimePicker = shouldPrecomposeTimePickerWarmup,
                previewTasks = previewTasks,
                timelineTasks = sortedTasks,
                now = now,
                displayNightMode = isNightMode,
                hasTasks = tasks.isNotEmpty(),
                selectedHour = draftStartHour,
                selectedMinute = draftStartMinute,
                minSelectableTotalMinutes = earliestDraftEndTotalMinutes,
                occupiedTimes = unavailableEndTimes(tasksExcludingEditing, earliestDraftEndTotalMinutes),
                appLanguage = appLanguage,
            )
        }
    }















    if (showDeleteDialog) {
        DeleteProjectDialog(
            tasks = sortedTasks,
            pendingDelete = taskPendingDelete,
            onSelectTask = { taskPendingDelete = it },
            onDismiss = {
                showDeleteDialog = false
                taskPendingDelete = null
            },
            onConfirmDelete = {
                val deletingId = taskPendingDelete?.id
                if (deletingId != null) {
                    tasks = tasks.filterNot { it.id == deletingId }
                }
                showDeleteDialog = false
                taskPendingDelete = null
                screenMode = MainScreenMode.Normal
            },
        )
    }
    if (showEditDialog) {
        EditProjectDialog(
            tasks = sortedTasks,
            pendingEdit = taskPendingEdit,
            onSelectTask = { taskPendingEdit = it },
            onDismiss = {
                showEditDialog = false
                taskPendingEdit = null
            },
            onConfirmEdit = {
                taskPendingEdit?.let { beginEditing(it) }
            },
        )
    }

    } // CompositionLocalProvider
}








@Composable
private fun PlaceholderFeaturePage(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.36f),
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.14f),
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun HiddenWarmupPrecompose(
    shouldPrecomposeClockFace: Boolean,
    shouldPrecomposeTimeline: Boolean,
    shouldPrecomposeModeControls: Boolean,
    shouldPrecomposeTimePicker: Boolean,
    previewTasks: List<TimeTask>,
    timelineTasks: List<TimeTask>,
    now: LocalTime,
    displayNightMode: Boolean,
    hasTasks: Boolean,
    selectedHour: Int,
    selectedMinute: Int,
    minSelectableTotalMinutes: Int,
    occupiedTimes: Set<Int>,
    appLanguage: AppLanguage = LocalAppLanguage.current,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = 0f
                scaleX = 0.98f
                scaleY = 0.98f
                translationX = -10000f
                translationY = -10000f
            },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (shouldPrecomposeClockFace) {
                ClockFace(
                    isNightMode = displayNightMode,
                    tasks = previewTasks,
                    currentTime = now,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    modeTransitionStyle = ClockModeTransitionStyle.None,
                    pulseTriggerKey = 0,
                    renderQuality = ClockFaceRenderQuality.Reduced,
                )
            }

            if (shouldPrecomposeTimeline) {
                TimelinePreview(
                    tasks = timelineTasks,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                )
            }

            if (shouldPrecomposeModeControls) {
                ModeToggleRow(
                    isNightMode = displayNightMode,
                    onNightModeChange = {},
                )
                PagerIndicatorRow(
                    currentPage = 0,
                    pageCount = 2,
                )
                SetInlineActionsRow(
                    hasTasks = hasTasks,
                    modifier = Modifier.fillMaxWidth(),
                    onNewProject = {},
                    onEditProject = {},
                    onDeleteProject = {},
                )
            }

            if (shouldPrecomposeTimePicker) {
                SnapTimePicker(
                    selectedHour = selectedHour,
                    selectedMinute = selectedMinute,
                    minTotalMinutes = minSelectableTotalMinutes,
                    occupiedTimes = occupiedTimes,
                    isDarkMode = displayNightMode,
                    appLanguage = appLanguage,
                    onHourSelected = {},
                    onMinuteSelected = {},
                )
            }
        }
    }
}

@Composable
private fun EditProjectDialog(
    tasks: List<TimeTask>,
    pendingEdit: TimeTask?,
    onSelectTask: (TimeTask) -> Unit,
    onDismiss: () -> Unit,
    onConfirmEdit: () -> Unit,
) {
    val outlinedContainerColor = neutralOutlinedButtonContainerColor()
    val outlinedContentColor = neutralOutlinedButtonContentColor()
    val outlinedBorderColor = neutralOutlinedBorderColor()
    val filledContainerColor = neutralFilledButtonContainerColor()
    val filledContentColor = neutralFilledButtonContentColor()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit project",
                fontWeight = FontWeight.SemiBold,
            )
        },
        text = {
            if (tasks.isEmpty()) {
                Text(
                    text = "There are no saved projects to edit yet.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Choose one project to update.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                        fontSize = 13.sp,
                    )
                    tasks.forEach { task ->
                        val selected = pendingEdit?.id == task.id
                        OutlinedButton(
                            onClick = { onSelectTask(task) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                containerColor = outlinedContainerColor,
                                contentColor = outlinedContentColor,
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selected) task.color else outlinedBorderColor,
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(10.dp)
                                            .background(task.color, CircleShape),
                                    )
                                    Text(
                                        text = task.title,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Text(
                                    text = task.prettyTimeRange(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmEdit,
                enabled = pendingEdit != null && tasks.isNotEmpty(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = filledContainerColor,
                    contentColor = filledContentColor,
                ),
            ) {
                Text("Edit")
            }
        },
        shape = RoundedCornerShape(24.dp),
    )
}





@Composable
private fun DeleteProjectDialog(
    tasks: List<TimeTask>,
    pendingDelete: TimeTask?,
    onSelectTask: (TimeTask) -> Unit,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    val outlinedContainerColor = neutralOutlinedButtonContainerColor()
    val outlinedContentColor = neutralOutlinedButtonContentColor()
    val outlinedBorderColor = neutralOutlinedBorderColor()
    val filledContainerColor = neutralFilledButtonContainerColor()
    val filledContentColor = neutralFilledButtonContentColor()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete project",
                fontWeight = FontWeight.SemiBold,
            )
        },
        text = {
            if (tasks.isEmpty()) {
                Text(
                    text = "There are no saved projects to delete yet.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Choose one project to remove from the clock.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                        fontSize = 13.sp,
                    )
                    tasks.forEach { task ->
                        val selected = pendingDelete?.id == task.id
                        OutlinedButton(
                            onClick = { onSelectTask(task) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                containerColor = outlinedContainerColor,
                                contentColor = outlinedContentColor,
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (selected) task.color else outlinedBorderColor,
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(10.dp)
                                            .background(task.color, CircleShape),
                                    )
                                    Text(
                                        text = task.title,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Text(
                                    text = task.prettyTimeRange(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                enabled = pendingDelete != null && tasks.isNotEmpty(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = filledContainerColor,
                    contentColor = filledContentColor,
                ),
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                    contentColor = outlinedContentColor,
                ),
            ) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(24.dp),
    )
}




@Composable
private fun ModeToggleRow(

    isNightMode: Boolean,
    onNightModeChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .widthIn(min = 132.dp, max = 156.dp)
            .wrapContentWidth(Alignment.Start)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.56f),
                shape = RoundedCornerShape(15.dp),
            )
            .padding(horizontal = 3.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompactModeChip(
            text = "☀",
            timeRange = "6:00-18:00",
            selected = !isNightMode,
            onClick = { onNightModeChange(false) },
        )
        CompactModeChip(
            text = "🌙",
            timeRange = "18:00-6:00",
            selected = isNightMode,
            onClick = { onNightModeChange(true) },
        )


    }
}

@Composable
private fun CompactModeChip(
    modifier: Modifier = Modifier,
    text: String,
    timeRange: String?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.14f)
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.62f)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
    }

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp),
            )
            .background(
                color = containerColor,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 9.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Text(
            text = text,
            color = if (selected) contentColor else contentColor.copy(alpha = 0.76f),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 14.sp,
        )

        if (timeRange != null) {
            Text(
                text = timeRange,
            color = if (selected) {
                contentColor.copy(alpha = 0.42f)
            } else {
                contentColor.copy(alpha = 0.30f)
            },
            fontWeight = FontWeight.Medium,
            fontSize = 8.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            )
        }
    }
}















@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DraftProjectCard(
    isEditing: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    selectedColor: Color,
    onSelectColor: (Color) -> Unit,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    earliestEndTotalMinutes: Int,
    occupiedStartTimes: Set<Int>,
    occupiedEndTimes: Set<Int>,
    draftColor: Color,
    timeError: String?,
    onSelectStartTime: (Int) -> Unit,
    onSelectEndTime: (Int) -> Unit,
    onSave: () -> Unit,
    appLanguage: AppLanguage,
) {
    val durationMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)
    val durationLabel = formatDurationLabel(durationMinutes)

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = appLanguage.strColorLabel(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(14.dp)
                            .height(14.dp)
                            .background(draftColor, CircleShape),
                    )
                    Text(
                        text = appLanguage.strColorSelected(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                taskColorPalette.forEach { optionColor ->
                    val isSelected = optionColor == selectedColor
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0.82f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
                        label = "color-scale",
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                            .then(
                                if (isSelected) Modifier.border(
                                    width = 2.5.dp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                                    shape = CircleShape,
                                ) else Modifier
                            )
                            .background(optionColor, CircleShape)
                            .clickable { onSelectColor(optionColor) },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.92f),
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Project title") },
                placeholder = { Text("What are you about to do?") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Time",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = durationLabel,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            DualTimePickerRow(
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                earliestEndTotalMinutes = earliestEndTotalMinutes,
                occupiedStartTimes = occupiedStartTimes,
                occupiedEndTimes = occupiedEndTimes,
                onSelectStartTime = onSelectStartTime,
                onSelectEndTime = onSelectEndTime,
            )
            if (timeError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = timeError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onSave,
                    enabled = timeError == null,
                    shape = RoundedCornerShape(18.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = neutralFilledButtonContainerColor(),
                        contentColor = neutralFilledButtonContentColor(),
                    ),
                ) {
                    Text(if (isEditing) "Save changes" else "Save project")
                }

            }
        }
    }
}




@Composable
private fun DualTimePickerRow(
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    earliestEndTotalMinutes: Int,
    occupiedStartTimes: Set<Int>,
    occupiedEndTimes: Set<Int>,
    onSelectStartTime: (Int) -> Unit,
    onSelectEndTime: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CompactTimeField(
            label = "Start",
            value = formatHourMinute(startHour, startMinute),
            occupiedTimes = occupiedStartTimes,
            modifier = Modifier.weight(1f),
            onTimeConfirmed = onSelectStartTime,
        )
        CompactTimeField(
            label = "End",
            value = formatHourMinute(endHour, endMinute),
            minTotalMinutes = earliestEndTotalMinutes,
            occupiedTimes = occupiedEndTimes,
            modifier = Modifier.weight(1f),
            onTimeConfirmed = onSelectEndTime,
        )
    }
}

@Composable
private fun CompactTimeField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    minTotalMinutes: Int = 0,
    occupiedTimes: Set<Int> = emptySet(),
    onTimeConfirmed: (Int) -> Unit,
) {
    var pickerOpen by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier.clickable { pickerOpen = true },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.3.sp,
            )
        }
    }

    if (pickerOpen) {
        CompactTimePickerDialog(
            initialTotalMinutes = nearestSelectableTime(
                preferred = max(minTotalMinutes, parseHourMinute(value)),
                minTotalMinutes = minTotalMinutes,
                occupiedTimes = occupiedTimes,
            ),
            minTotalMinutes = minTotalMinutes,
            occupiedTimes = occupiedTimes,
            onDismiss = { pickerOpen = false },
            onConfirm = {
                pickerOpen = false
                onTimeConfirmed(it)
            },
        )
    }
}



@Composable
private fun CompactTimePickerDialog(
    initialTotalMinutes: Int,
    minTotalMinutes: Int,
    occupiedTimes: Set<Int>,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    appLanguage: AppLanguage = LocalAppLanguage.current,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    val dialogContainerColor = if (isDarkMode) {
        colorScheme.surface.copy(alpha = 0.96f)
    } else {
        Color(0xFFF8F9FD).copy(alpha = 0.98f)
    }
    val titleColor = if (isDarkMode) colorScheme.onSurface.copy(alpha = 0.96f) else Color(0xFF111827)
    val subtitleColor = if (isDarkMode) colorScheme.onSurface.copy(alpha = 0.92f) else Color(0xFF4B5563)
    val confirmButtonColor = neutralFilledButtonContainerColor()

    val confirmButtonContentColor = neutralFilledButtonContentColor()




    val initialResolved = remember(initialTotalMinutes, minTotalMinutes, occupiedTimes) {
        nearestSelectableTime(
            preferred = initialTotalMinutes,
            minTotalMinutes = minTotalMinutes,
            occupiedTimes = occupiedTimes,
        )
    }

    var selectedHour by remember(initialResolved) { mutableIntStateOf(initialResolved / 60) }
    var selectedMinute by remember(initialResolved) { mutableIntStateOf(initialResolved % 60) }

    LaunchedEffect(selectedHour, minTotalMinutes, occupiedTimes) {
        val validMinutes = selectableMinutesForHour(selectedHour, minTotalMinutes, occupiedTimes)
        if (validMinutes.isEmpty()) {
            val fallback = nearestSelectableTime(
                preferred = selectedHour * 60 + selectedMinute,
                minTotalMinutes = minTotalMinutes,
                occupiedTimes = occupiedTimes,
            )
            selectedHour = fallback / 60
            selectedMinute = fallback % 60
        } else if (selectedMinute !in validMinutes) {
            selectedMinute = validMinutes.minByOrNull { kotlin.math.abs(it - selectedMinute) } ?: validMinutes.first()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Select time",
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Text(
                    text = formatHourMinute(selectedHour, selectedMinute),
                    color = subtitleColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                SnapTimePicker(
                    selectedHour = selectedHour,
                    selectedMinute = selectedMinute,
                    minTotalMinutes = minTotalMinutes,
                    occupiedTimes = occupiedTimes,
                    isDarkMode = isDarkMode,
                    appLanguage = appLanguage,
                    onHourSelected = { selectedHour = it },
                    onMinuteSelected = { selectedMinute = it },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = {
                            onConfirm(
                                nearestSelectableTime(
                                    preferred = selectedHour * 60 + selectedMinute,
                                    minTotalMinutes = minTotalMinutes,
                                    occupiedTimes = occupiedTimes,
                                ),
                            )
                        },
                        modifier = Modifier.widthIn(min = 112.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = confirmButtonColor,
                            contentColor = confirmButtonContentColor,
                        ),
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
        shape = RoundedCornerShape(28.dp),
        containerColor = dialogContainerColor,
        tonalElevation = 0.dp,
        titleContentColor = titleColor,
        textContentColor = titleColor,
    )
}






@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalendarReservedPage(
    selectedDate: LocalDate,
    tasks: List<TimeTask>,
    onSelectedDateChange: (LocalDate) -> Unit,
    onOpenClockDay: () -> Unit,
    onTaskClick: (TimeTask) -> Unit,
    onTaskDelete: (TimeTask) -> Unit,
    onTaskEdit: (TimeTask) -> Unit,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier,
    appLanguage: AppLanguage = LocalAppLanguage.current,
) {
    val today = remember { LocalDate.now() }
    var displayedMonth by rememberSaveable { mutableStateOf(YearMonth.from(selectedDate)) }

    val monthCells = remember(displayedMonth) { calendarMonthCells(displayedMonth) }
    val activityCounts = remember(tasks) { calendarActivityCounts(tasks) }
    val selectedDateTasks = remember(tasks, selectedDate) {
        tasks.filter { it.date == selectedDate }.sortedBy { it.startTotalMinutes() }
    }

    // — Day summary stats —
    val totalPlannedMinutes = remember(selectedDateTasks) {
        selectedDateTasks.sumOf { (it.normalizedEndTotalMinutes() - it.startTotalMinutes()).coerceAtLeast(0) }
    }
    val totalFreeMinutes = (24 * 60 - totalPlannedMinutes).coerceAtLeast(0)

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // — Month header: compact, no card wrapper —
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = appLanguage.strMonthYear(displayedMonth.year, displayedMonth.monthValue),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TextButton(onClick = { displayedMonth = displayedMonth.minusMonths(1) }) { Text("\u2039") }
                TextButton(onClick = {
                    displayedMonth = YearMonth.from(today)
                    onSelectedDateChange(today)
                }) { Text(appLanguage.strToday(), fontSize = 12.sp) }
                TextButton(onClick = { displayedMonth = displayedMonth.plusMonths(1) }) { Text("\u203a") }
            }
        }

        // — Weekday labels —
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            (1..7).forEach { dow ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = appLanguage.strWeekdayHeader(dow),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        // — Month grid —
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            monthCells.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    week.forEach { date ->
                        CalendarMonthDayCell(
                            modifier = Modifier.weight(1f),
                            date = date,
                            selected = date == selectedDate,
                            isToday = date == today,
                            activityCount = date?.let { activityCounts[it] } ?: 0,
                            onClick = {
                                if (date != null) {
                                    onSelectedDateChange(date)
                                    displayedMonth = YearMonth.from(date)
                                }
                            },
                        )
                    }
                }
            }
        }

        // ── Mini 24h timeline bar ──
        CalendarDayTimelineBar(
            tasks = selectedDateTasks,
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp),
        )

        // — Selected day section: header + summary + tasks —
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Date header with add button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${selectedDate.monthValue}/${selectedDate.dayOfMonth} ${appLanguage.strWeekday(selectedDate.dayOfWeek.value)}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    if (selectedDateTasks.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = appLanguage.strItems(selectedDateTasks.size),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Summary stats
                    Text(
                        text = "${formatDurationLabel(totalPlannedMinutes)} ${appLanguage.strPlanned()} \u00b7 ${formatDurationLabel(totalFreeMinutes)} ${appLanguage.strFree()}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                        fontSize = 11.sp,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    // Quick-add button
                    OutlinedButton(
                        onClick = onAddTask,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                    ) {
                        Text("+ ${appLanguage.strAddTask()}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            if (selectedDateTasks.isEmpty()) {
                // — Empty state with guidance —
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = appLanguage.strNoTasksEmpty(),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = appLanguage.strAddFirstTask(),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        fontSize = 13.sp,
                    )
                }
            } else {
                selectedDateTasks.forEach { task ->
                    SwipeableTaskRow(
                        task = task,
                        appLanguage = appLanguage,
                        onClick = { onTaskClick(task) },
                        onDelete = { onTaskDelete(task) },
                        onEdit = { onTaskEdit(task) },
                    )
                }

                TextButton(
                    onClick = onOpenClockDay,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(appLanguage.strViewClock(), fontSize = 12.sp)
                }
            }
        }
    }
}

private fun calendarActivityCounts(tasks: List<TimeTask>): Map<LocalDate, Int> =
    tasks.groupingBy { it.date }.eachCount()


private fun calendarMonthCells(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val leadingEmpty = firstDay.dayOfWeek.value - 1
    val days = List(month.lengthOfMonth()) { index -> month.atDay(index + 1) }
    val totalCells = leadingEmpty + days.size
    val trailingEmpty = (7 - totalCells % 7) % 7
    return List(leadingEmpty) { null } + days + List(trailingEmpty) { null }
}



@Composable
private fun CalendarDayTimelineBar(
    tasks: List<TimeTask>,
    modifier: Modifier = Modifier,
) {
    val isDarkMode = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val trackColor = if (isDarkMode) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.32f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    }
    val dayDividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    val nightDividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)

    Box(modifier = modifier.clip(RoundedCornerShape(6.dp))) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val fullMinutes = 24 * 60f

            // Background track
            drawRect(color = trackColor)

            // Day/Night divider at 6:00 and 18:00
            val dayStartX = size.width * (6 * 60f / fullMinutes)
            val nightStartX = size.width * (18 * 60f / fullMinutes)
            drawLine(
                color = dayDividerColor,
                start = Offset(dayStartX, 0f),
                end = Offset(dayStartX, size.height),
                strokeWidth = 1.dp.toPx(),
            )
            drawLine(
                color = nightDividerColor,
                start = Offset(nightStartX, 0f),
                end = Offset(nightStartX, size.height),
                strokeWidth = 1.dp.toPx(),
            )

            // Task segments
            val cornerRadius = 3.dp.toPx()
            tasks.forEach { task ->
                val startMin = task.startTotalMinutes().toFloat()
                val endMin = task.normalizedEndTotalMinutes().coerceAtMost(24 * 60).toFloat()
                val left = size.width * (startMin / fullMinutes)
                val right = size.width * (endMin / fullMinutes)
                val segmentWidth = (right - left).coerceAtLeast(cornerRadius * 2)
                drawRoundRect(
                    color = task.color.copy(alpha = 0.82f),
                    topLeft = Offset(left, 1.dp.toPx()),
                    size = Size(segmentWidth, size.height - 2.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            }
        }

        // "D" and "N" labels — overlaid as Compose Text
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 0:00–6:00 midnight zone (skip)
            Spacer(modifier = Modifier.weight(6f))
            // 6:00–18:00 day zone
            Text(
                text = "D",
                color = labelColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(12f),
            )
            // 18:00–24:00 night zone
            Text(
                text = "N",
                color = labelColor,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(6f),
            )
        }
    }
}






@Composable
private fun CalendarMonthDayCell(
    date: LocalDate?,
    selected: Boolean,
    isToday: Boolean,
    activityCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = when {
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        isToday -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f)
        else -> Color.Transparent
    }
    val borderColor = when {
        selected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.36f)
        isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .then(if (date != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        if (date != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = when {
                        selected -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontSize = 13.sp,
                    fontWeight = if (selected || isToday) FontWeight.SemiBold else FontWeight.Normal,
                )
                if (activityCount > 0) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(4.dp)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary else ClockBlue,
                                CircleShape,
                            ),
                    )
                }
            }
        }
    }
}

// — Swipeable task row with left-swipe-to-delete —
@Composable
private fun SwipeableTaskRow(
    task: TimeTask,
    appLanguage: AppLanguage,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val maxSwipeDistance = 200f
    val deleteThreshold = 160f
    val isDarkMode = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val deleteBgColor = Color(0xFFE53935).copy(alpha = if (isDarkMode) 0.85f else 0.9f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (offsetX < -deleteThreshold) deleteBgColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX < -deleteThreshold) {
                            onDelete()
                        }
                        offsetX = 0f
                    },
                    onDragCancel = {
                        offsetX = 0f
                    },
                ) { _, dragAmount ->
                    val newOffset = (offsetX + dragAmount).coerceIn(-maxSwipeDistance, 0f)
                    offsetX = newOffset
                }
            }
    ) {
        // Background: delete icon revealed on swipe
        if (offsetX < -10f) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = appLanguage.strDelete(),
                    tint = Color.White.copy(alpha = (kotlin.math.abs(offsetX) / deleteThreshold).coerceIn(0f, 1f)),
                    modifier = Modifier.padding(vertical = 14.dp),
                )
            }
        }

        // Foreground: task content
        Row(
            modifier = Modifier
                .offset(x = with(LocalDensity.current) { offsetX.toDp() })
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 6.dp, horizontal = 4.dp)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(task.color, RoundedCornerShape(2.dp)),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = task.prettyTimeRange(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.48f),
                    fontSize = 12.sp,
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MineReservedPage(
    modifier: Modifier = Modifier,
    selectedGoal: String,
    syncOnWifiOnly: Boolean,
    autoArchiveEnabled: Boolean,
    todayTaskStats: MineTodayTaskStats,
    currentGoalAccent: Color,
    isNightMode: Boolean,
    appLanguage: AppLanguage,
    onGoalChange: (String) -> Unit,
    onSyncChange: (Boolean) -> Unit,
    onAutoArchiveChange: (Boolean) -> Unit,
    onNightModeChange: (Boolean) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onNavigateToClock: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
) {
    val goals = remember { mineGoalOptions() }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // — Identity header: minimal —
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .background(currentGoalAccent.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "W",
                    color = currentGoalAccent,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column {
                Text(
                    text = "Wanxing",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = if (todayTaskStats.totalTaskCount == 0) appLanguage.strNoScheduleYet()
                    else "${appLanguage.strItems(todayTaskStats.totalTaskCount)} · ${formatDurationLabel(todayTaskStats.totalPlannedMinutes)}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f),
                    fontSize = 13.sp,
                )
            }
        }

        // — Today at a glance —
        if (todayTaskStats.totalTaskCount > 0) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                todayTaskStats.activeTaskTitle?.let { title ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToClock() },
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .height(8.dp)
                                .background(currentGoalAccent, CircleShape),
                        )
                        Text(
                            text = appLanguage.strActive(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                todayTaskStats.nextTaskTitle?.let { next ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToClock() },
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .height(8.dp)
                                .background(ClockGreen, CircleShape),
                        )
                        Text(
                            text = appLanguage.strNext(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = buildString {
                                append(next)
                                todayTaskStats.nextTaskStartLabel?.let { append(" · $it") }
                            },
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        // — Quick links to other pages: flat rows, no card —
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToClock() }
                    .background(currentGoalAccent.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = currentGoalAccent,
                    modifier = Modifier.width(18.dp).height(18.dp),
                )
                Text(
                    text = appLanguage.strClock(),
                    color = currentGoalAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToCalendar() }
                    .background(ClockTeal.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = ClockTeal,
                    modifier = Modifier.width(18.dp).height(18.dp),
                )
                Text(
                    text = appLanguage.strCalendar(),
                    color = ClockTeal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        // — Divider —
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
        )

        // — Goal selection: FlowRow for flexible wrapping —
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = appLanguage.strStrategy(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                goals.forEach { goal ->
                    MineGoalChip(
                        option = goal,
                        selected = selectedGoal == goal.label,
                        onClick = { onGoalChange(goal.label) },
                    )
                }
            }
        }

        // — Settings: inline toggles, no dialog —
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = appLanguage.strSettings(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )

            MineSettingToggleRow(
                icon = Icons.Filled.Tune,
                title = appLanguage.strWifiSyncTitle(),
                subtitle = appLanguage.strWifiSync(syncOnWifiOnly),
                accent = ClockBlue,
                checked = syncOnWifiOnly,
                onCheckedChange = onSyncChange,
                statusLabel = appLanguage.strWifiLabel(syncOnWifiOnly),
            )
            MineSettingToggleRow(
                icon = Icons.Filled.DateRange,
                title = appLanguage.strAutoArchiveTitle(),
                subtitle = appLanguage.strAutoArchive(autoArchiveEnabled),
                accent = ClockGreen,
                checked = autoArchiveEnabled,
                onCheckedChange = onAutoArchiveChange,
                statusLabel = appLanguage.strWeeklyManual(autoArchiveEnabled),
            )

            // Theme toggle inline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(44.dp)
                            .background(ClockYellow.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = appLanguage.strAppearance(),
                            tint = ClockYellow,
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = appLanguage.strAppearance(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = if (isNightMode) appLanguage.strNightMode() else appLanguage.strDayMode(),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                        )
                    }
                }
                ModeToggleRow(
                    isNightMode = isNightMode,
                    onNightModeChange = { onNightModeChange(!isNightMode) },
                )
            }

            // Language selector inline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(44.dp)
                            .background(ClockPurple.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = appLanguage.strLanguage(),
                            tint = ClockPurple,
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = appLanguage.strLanguage(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = appLanguage.displayName,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.56f),
                            shape = RoundedCornerShape(15.dp),
                        )
                        .padding(horizontal = 3.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppLanguage.values().forEach { lang ->
                        val selected = appLanguage == lang
                        CompactModeChip(
                            text = lang.code,
                            timeRange = null,
                            selected = selected,
                            onClick = { onLanguageChange(lang) },
                        )
                    }
                }
            }
        }
    }
}




private data class MineTodayTaskStats(
    val totalTaskCount: Int,
    val totalPlannedMinutes: Int,
    val activeTaskTitle: String?,
    val nextTaskTitle: String?,
    val nextTaskStartLabel: String?,
)

private data class MineGoalOption(
    val label: String,
    val hint: String,
    val accent: Color,
)

private fun mineGoalOptions(): List<MineGoalOption> = listOf(
    MineGoalOption(label = "Steady", hint = "For class and internship balance", accent = ClockTeal),
    MineGoalOption(label = "Balanced", hint = "Default pace for steady progress", accent = ClockPurple),
    MineGoalOption(label = "Sprint", hint = "For short focused pushes", accent = ClockRed),
)

private fun buildDayTaskStats(
    tasks: List<TimeTask>,
    now: LocalTime,
): MineTodayTaskStats {
    val sortedTasks = tasks.sortedBy { it.startTotalMinutes() }
    val activeTask = sortedTasks.firstOrNull { isTaskActive(it, now) }
    val nextTask = sortedTasks
        .filter { !isTaskActive(it, now) }
        .minByOrNull { minutesUntilTask(it, now) }

    return MineTodayTaskStats(
        totalTaskCount = sortedTasks.size,
        totalPlannedMinutes = sortedTasks.sumOf { task ->
            (task.normalizedEndTotalMinutes() - task.startTotalMinutes()).coerceAtLeast(0)
        },
        activeTaskTitle = activeTask?.title,
        nextTaskTitle = nextTask?.title,
        nextTaskStartLabel = nextTask?.let { formatHourMinute(it.startHour, it.startMinute) },
    )
}

private fun suggestedDraftTitle(
    selectedGoal: String,
    isNightMode: Boolean,
): String {
    val prefix = when (selectedGoal) {
        "Steady" -> if (isNightMode) "Steady Night" else "Steady Block"
        "Sprint" -> if (isNightMode) "Night Sprint" else "Sprint Focus"
        else -> if (isNightMode) "Night Flow" else "Balanced Flow"
    }
    return "$prefix Plan"
}

private fun preferredDraftColor(selectedGoal: String): Color = when (selectedGoal) {
    "Steady" -> ClockTeal
    "Sprint" -> ClockRed
    else -> ClockPurple
}

private fun defaultDraftRangeForGoal(
    selectedGoal: String,
    isNightMode: Boolean,
    syncOnWifiOnly: Boolean,
): Pair<Int, Int> {
    val baseStart = when {
        isNightMode && selectedGoal == "Sprint" -> 20 * 60
        isNightMode -> 19 * 60
        selectedGoal == "Steady" -> 9 * 60
        selectedGoal == "Sprint" -> 10 * 60
        else -> 8 * 60 + 30
    }
    val duration = when {
        !syncOnWifiOnly && selectedGoal == "Sprint" -> 150
        !syncOnWifiOnly -> 120
        selectedGoal == "Steady" -> 75
        else -> 90
    }
    val end = (baseStart + duration).coerceAtMost(23 * 60 + 59)
    return baseStart to end.coerceAtLeast(baseStart + 1)
}








@Composable
private fun MineStatusRow(
    label: String,
    value: String,
    accent: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(8.dp)
                .height(8.dp)
                .background(accent, CircleShape),
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}




@Composable
private fun MineGoalChip(
    option: MineGoalOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    OutlinedCard(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .widthIn(min = 112.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (selected) option.accent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, if (selected) option.accent.copy(alpha = 0.28f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = option.label,
                color = if (selected) option.accent else MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = option.hint,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                fontSize = 11.sp,
                lineHeight = 16.sp,
            )
        }
    }
}


@Composable
private fun MineSettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accent: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    statusLabel: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(44.dp)
                .background(accent.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = accent,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp,
                lineHeight = 18.sp,
            )
            Text(
                text = statusLabel,
                color = accent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }

        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}






@Composable
private fun ReservedRootPage(


    title: String,
    subtitle: String,
    sectionLabel: String,
    slots: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        PlaceholderFeaturePage(
            title = title,
            subtitle = subtitle,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = sectionLabel,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )

        slots.forEachIndexed { index, slot ->
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "Slot ${index + 1}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.54f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = slot,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}








@Composable
private fun SetInlineActionsRow(
    hasTasks: Boolean,
    modifier: Modifier = Modifier,
    onNewProject: () -> Unit,
    onEditProject: () -> Unit,
    onDeleteProject: () -> Unit,
) {
    val outlinedContainerColor = neutralOutlinedButtonContainerColor()
    val outlinedContentColor = neutralOutlinedButtonContentColor()
    val outlinedBorderColor = neutralOutlinedBorderColor()
    val helperTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    val centerScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = 0.72f,
            stiffness = 520f,
        ),
        label = "set-actions-center-scale",
    )
    val sideScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = 0.76f,
            stiffness = 460f,
        ),
        label = "set-actions-side-scale",
    )
    val sideAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 200, delayMillis = 50),
        label = "set-actions-side-alpha",
    )
    val centerAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 180),
        label = "set-actions-center-alpha",
    )

    Column(
        modifier = modifier.wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SetActionSegmentButton(
                icon = Icons.Filled.Add,
                contentDescription = "New project",
                enabled = true,
                shape = RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp, topEnd = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer {
                        translationX = -18f
                        scaleX = sideScale
                        scaleY = sideScale
                        alpha = sideAlpha
                    },
                containerColor = outlinedContainerColor,
                contentColor = outlinedContentColor,
                borderColor = outlinedBorderColor,
                onClick = onNewProject,
            )

            SetActionSegmentButton(
                icon = Icons.Filled.Edit,
                contentDescription = "Edit project",
                enabled = hasTasks,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .offset(x = (-1).dp)
                    .graphicsLayer {
                        scaleX = centerScale
                        scaleY = centerScale
                        alpha = centerAlpha
                    },
                containerColor = outlinedContainerColor,
                contentColor = outlinedContentColor,
                borderColor = outlinedBorderColor,
                onClick = onEditProject,
            )

            SetActionSegmentButton(
                icon = Icons.Outlined.DeleteOutline,
                contentDescription = "Delete project",
                enabled = hasTasks,
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp, topEnd = 18.dp, bottomEnd = 18.dp),
                modifier = Modifier
                    .weight(1f)
                    .offset(x = (-2).dp)
                    .graphicsLayer {
                        translationX = 18f
                        scaleX = sideScale
                        scaleY = sideScale
                        alpha = sideAlpha
                    },
                containerColor = outlinedContainerColor,
                contentColor = outlinedContentColor,
                borderColor = outlinedBorderColor,
                onClick = onDeleteProject,
            )
        }

        if (!hasTasks) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "No saved projects yet.",
                color = helperTextColor,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun TopSetActionsCluster(
    hasTasks: Boolean,
    onNewProject: () -> Unit,
    onEditProject: () -> Unit,
    onDeleteProject: () -> Unit,
) {
    val outlinedContainerColor = neutralOutlinedButtonContainerColor()
    val outlinedContentColor = neutralOutlinedButtonContentColor()
    val outlinedBorderColor = neutralOutlinedBorderColor()
    val buttonWidth = 72.dp
    val buttonHeight = 52.dp
    val spacing = 8.dp

    Box(
        modifier = Modifier
            .width(buttonWidth * 2 + spacing)
            .height(buttonHeight),
    ) {
        SetActionSegmentButton(
            icon = Icons.Outlined.DeleteOutline,
            contentDescription = "Delete project",
            enabled = hasTasks,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight)
                .align(Alignment.BottomStart),
            containerColor = outlinedContainerColor,
            contentColor = outlinedContentColor,
            borderColor = outlinedBorderColor,
            onClick = onDeleteProject,
        )

        SetActionSegmentButton(
            icon = Icons.Filled.Edit,
            contentDescription = "Edit project",
            enabled = hasTasks,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight)
                .align(Alignment.BottomEnd),
            containerColor = outlinedContainerColor,
            contentColor = outlinedContentColor,
            borderColor = outlinedBorderColor,
            onClick = onEditProject,
        )

        SetActionSegmentButton(
            icon = Icons.Filled.Add,
            contentDescription = "New project",
            enabled = true,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight)
                .align(Alignment.BottomEnd)
                .offset(y = -(buttonHeight + spacing)),
            containerColor = outlinedContainerColor,
            contentColor = outlinedContentColor,
            borderColor = outlinedBorderColor,
            onClick = onNewProject,
        )
    }
}

@Composable
private fun SetActionSegmentButton(


    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = shape,
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.42f),
            disabledContentColor = contentColor.copy(alpha = 0.45f),
        ),
        border = BorderStroke(1.dp, if (enabled) borderColor else borderColor.copy(alpha = 0.35f)),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.padding(vertical = 14.dp),
        )
    }
}

@Composable
private fun SetTriggerButton(

    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(72.dp)
            .height(52.dp),
        shape = RoundedCornerShape(18.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Icon(
            imageVector = Icons.Filled.Tune,
            contentDescription = "Set actions",
            modifier = Modifier.padding(vertical = 6.dp),
        )
    }
}


@Composable
private fun PagerIndicatorRow(

    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
) {
    val activeIndicatorColor = MaterialTheme.colorScheme.primary
    val inactiveIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isCurrentPage = index == currentPage
            val indicatorSize = if (isCurrentPage) 10.dp else 7.dp

            Box(
                modifier = Modifier
                    .width(indicatorSize)
                    .height(indicatorSize)
                    .background(
                        if (isCurrentPage) activeIndicatorColor else inactiveIndicatorColor,
                        CircleShape,
                    ),
            )
            if (index != pageCount - 1) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }

}





@Composable
private fun ProjectSummaryCard(
    sectionTitle: String,
    task: TimeTask?,
) {
    // Flat layout — no card wrapper, just a divider-accented row
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f),
                RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(10.dp)
                    .background(
                        (task?.color ?: MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
                        CircleShape,
                    ),
            )
            Text(
                text = if (task == null) sectionTitle else "$sectionTitle: ${task.title}",
                color = if (task == null) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.34f)
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
        if (task != null) {
            Text(
                text = task.prettyTimeRange(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp),
            )
        }
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SnapTimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    minTotalMinutes: Int,
    occupiedTimes: Set<Int>,
    isDarkMode: Boolean,
    appLanguage: AppLanguage = LocalAppLanguage.current,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
) {
    val enabledHours = remember(minTotalMinutes, occupiedTimes) {
        (0..23).filter { hour -> selectableMinutesForHour(hour, minTotalMinutes, occupiedTimes).isNotEmpty() }
    }
    val effectiveHour = if (selectedHour in enabledHours) selectedHour else enabledHours.firstOrNull() ?: 0
    val enabledMinutes = remember(effectiveHour, minTotalMinutes, occupiedTimes) {
        selectableMinutesForHour(effectiveHour, minTotalMinutes, occupiedTimes)
    }
    val effectiveMinute = if (selectedMinute in enabledMinutes) selectedMinute else enabledMinutes.firstOrNull() ?: 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SnapWheelColumn(
            label = appLanguage.strHourLabel(),
            values = enabledHours,
            selectedValue = effectiveHour,
            modifier = Modifier.weight(1f),
            isDarkMode = isDarkMode,
            onSelected = onHourSelected,
        )
        SnapWheelColumn(
            label = appLanguage.strMinuteLabel(),
            values = enabledMinutes,
            selectedValue = effectiveMinute,
            modifier = Modifier.weight(1f),
            isDarkMode = isDarkMode,
            onSelected = onMinuteSelected,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SnapWheelColumn(
    label: String,
    values: List<Int>,
    selectedValue: Int,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    onSelected: (Int) -> Unit,
) {
    val rowHeight = 52.dp
    val visibleRows = 5
    val centerSlots = visibleRows / 2
    val initialIndex = values.indexOf(selectedValue).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val latestSelectedValue by rememberUpdatedState(selectedValue)
    val latestValues by rememberUpdatedState(values)
    val latestOnSelected by rememberUpdatedState(onSelected)
    val labelColor = if (isDarkMode) Color.White.copy(alpha = 0.56f) else Color(0xFF6B7280)
    val selectionHighlightColor = if (isDarkMode) {
        Color.White.copy(alpha = 0.10f)
    } else {
        Color(0xFFE9EEF8)
    }
    val overlayBaseColor = if (isDarkMode) Color(0xFF1A1B1F) else Color(0xFFF8F9FD)
    val selectedTextColor = if (isDarkMode) Color.White else Color(0xFF111827)
    val unselectedTextBaseColor = if (isDarkMode) Color.White else Color(0xFF374151)

    fun currentCenteredIndex(): Int? {
        val layoutInfo = listState.layoutInfo
        if (layoutInfo.visibleItemsInfo.isEmpty()) return null
        val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
        return layoutInfo.visibleItemsInfo
            .minByOrNull { item -> kotlin.math.abs((item.offset + item.size / 2) - viewportCenter) }
            ?.index
            ?.coerceIn(0, latestValues.lastIndex)
    }

    LaunchedEffect(values, selectedValue) {
        if (values.isEmpty()) return@LaunchedEffect
        delay(72)
        if (listState.isScrollInProgress) return@LaunchedEffect

        val targetIndex = values.indexOf(selectedValue).coerceAtLeast(0)
        val centeredIndex = currentCenteredIndex()
        if (centeredIndex != null && centeredIndex != targetIndex) {
            listState.animateScrollToItem(targetIndex)
        }
    }


    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collectLatest { scrolling ->
                if (scrolling) return@collectLatest
                delay(72)
                if (latestValues.isEmpty() || listState.isScrollInProgress) return@collectLatest

                val centeredIndex = currentCenteredIndex() ?: return@collectLatest
                val resolved = latestValues[centeredIndex]
                if (resolved != latestSelectedValue) {
                    latestOnSelected(resolved)
                    return@collectLatest
                }

                val targetIndex = latestValues.indexOf(latestSelectedValue).coerceAtLeast(0)
                if (centeredIndex != targetIndex) {
                    listState.animateScrollToItem(targetIndex)
                }
            }
    }


    Column(modifier = modifier) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(rowHeight * visibleRows),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight)
                    .align(Alignment.Center)
                    .background(
                        selectionHighlightColor,
                        RoundedCornerShape(16.dp),
                    ),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight * 1.55f)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                overlayBaseColor,
                                overlayBaseColor.copy(alpha = 0.78f),
                                Color.Transparent,
                            ),
                        ),
                    ),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight * 1.55f)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                overlayBaseColor.copy(alpha = 0.78f),
                                overlayBaseColor,
                            ),
                        ),
                    ),
            )
            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                contentPadding = PaddingValues(vertical = rowHeight * centerSlots),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(values.size) { index ->
                    val option = values[index]
                    val isSelected = option == selectedValue
                    val distance = kotlin.math.abs(index - values.indexOf(selectedValue))
                    val textAlpha = when (distance) {
                        0 -> 1f
                        1 -> 0.62f
                        2 -> 0.34f
                        else -> 0.18f
                    }
                    val textSize = when (distance) {
                        0 -> 30.sp
                        1 -> 24.sp
                        else -> 19.sp
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(rowHeight)
                            .clickable { onSelected(option) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = option.toTwoDigits(),
                            color = if (isSelected) selectedTextColor else unselectedTextBaseColor.copy(alpha = textAlpha),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                            fontSize = textSize,
                            letterSpacing = (-0.3).sp,
                        )
                    }
                }
            }
        }
    }
}










private fun nearestEnabledValue(
    values: List<Int>,
    preferred: Int,
    isValueEnabled: (Int) -> Boolean,
): Int {
    val exact = values.firstOrNull { it == preferred && isValueEnabled(it) }
    if (exact != null) return exact
    return values
        .filter(isValueEnabled)
        .minByOrNull { kotlin.math.abs(it - preferred) }
        ?: values.first()
}

private fun nearestSelectableForHour(
    hour: Int,
    currentMinute: Int,
    minTotalMinutes: Int,
    occupiedTimes: Set<Int>,
): Int {
    val minutes = selectableMinutesForHour(hour, minTotalMinutes, occupiedTimes)
    if (minutes.isEmpty()) {
        return nearestSelectableTime(
            preferred = hour * 60,
            minTotalMinutes = minTotalMinutes,
            occupiedTimes = occupiedTimes,
        )
    }
    val minute = minutes.minByOrNull { kotlin.math.abs(it - currentMinute) } ?: minutes.first()
    return hour * 60 + minute
}

private fun selectableMinutesForHour(
    hour: Int,
    minTotalMinutes: Int,
    occupiedTimes: Set<Int>,
): List<Int> {
    return (0..59).filter { minute ->
        val total = hour * 60 + minute
        total >= minTotalMinutes && total !in occupiedTimes
    }
}

private fun nearestSelectableTime(
    preferred: Int,
    minTotalMinutes: Int,
    occupiedTimes: Set<Int>,
): Int {
    val lowerBound = minTotalMinutes.coerceIn(0, 23 * 60 + 59)
    val boundedPreferred = preferred.coerceIn(lowerBound, 23 * 60 + 59)
    if (boundedPreferred !in occupiedTimes) return boundedPreferred
    for (distance in 1..(23 * 60 + 59)) {
        val earlier = boundedPreferred - distance
        if (earlier >= lowerBound && earlier !in occupiedTimes) return earlier
        val later = boundedPreferred + distance
        if (later <= 23 * 60 + 59 && later !in occupiedTimes) return later
    }
    return boundedPreferred
}

private fun unavailableStartTimes(tasks: List<TimeTask>): Set<Int> =
    tasks.flatMap { task ->
        val start = task.startTotalMinutes()
        val endExclusive = task.endTotalMinutes().coerceAtLeast(start + 1)
        (start until endExclusive).toList()
    }.toSet()

private fun unavailableEndTimes(
    tasks: List<TimeTask>,
    minTotalMinutes: Int,
): Set<Int> =
    (0 until minTotalMinutes).toSet() + unavailableStartTimes(tasks)

private fun nextAvailableTaskColor(


    tasks: List<TimeTask>,
    preferred: Color,
): Color {
    val usedColors = tasks.map { it.color }.toSet()
    if (preferred !in usedColors) return preferred
    return taskColorPalette.firstOrNull { it !in usedColors } ?: preferred
}



@Composable
private fun TimelinePreview(
    tasks: List<TimeTask>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp),
        ) {
            Text(
                text = "Today timeline",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (tasks.isEmpty()) "All-day view · no projects yet" else "All-day view · ${tasks.size} project(s)",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "No projects scheduled for today.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.52f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                ) {
                    items(tasks, key = { it.id }) { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(10.dp)
                                    .background(task.color, CircleShape),
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = task.title,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${task.prettyTimeRange()} · ${taskWindowLabel(task)}",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                                    fontSize = 13.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



private fun tasksForMode(
    tasks: List<TimeTask>,
    isNightMode: Boolean,
): List<TimeTask> {
    val windowStart = if (isNightMode) 18 * 60 else 6 * 60
    val windowEndExclusive = if (isNightMode) 30 * 60 else 18 * 60

    return tasks.filter { task ->
        val taskStart = task.startTotalMinutes()
        val taskEnd = task.normalizedEndTotalMinutes()
        val taskRanges = listOf(
            taskStart to taskEnd,
            taskStart + 24 * 60 to taskEnd + 24 * 60,
        )
        taskRanges.any { range ->
            val overlapStart = max(windowStart, range.first)
            val overlapEnd = kotlin.math.min(windowEndExclusive, range.second)
            overlapEnd > overlapStart
        }
    }
}

private fun taskWindowLabel(task: TimeTask): String {
    val touchesDay = overlapsWindow(task, windowStart = 6 * 60, windowEndExclusive = 18 * 60)
    val touchesNight = overlapsWindow(task, windowStart = 18 * 60, windowEndExclusive = 30 * 60)
    return when {
        touchesDay && touchesNight -> "day + night"
        touchesNight -> "night"
        else -> "day"
    }
}

private fun overlapsWindow(
    task: TimeTask,
    windowStart: Int,
    windowEndExclusive: Int,
): Boolean {
    val taskStart = task.startTotalMinutes()
    val taskEnd = task.normalizedEndTotalMinutes()
    val taskRanges = listOf(
        taskStart to taskEnd,
        taskStart + 24 * 60 to taskEnd + 24 * 60,
    )
    return taskRanges.any { range ->
        val overlapStart = max(windowStart, range.first)
        val overlapEnd = kotlin.math.min(windowEndExclusive, range.second)
        overlapEnd > overlapStart
    }
}




private fun remainingMinutes(task: TimeTask, now: LocalTime): Int {
    val end = task.normalizedEndTotalMinutes()
    val nowValue = nowMinute(now)
    val start = task.startTotalMinutes()
    val adjustedNow = if (end >= 24 * 60 && nowValue < start) nowValue + 24 * 60 else nowValue
    return end - adjustedNow
}

private fun interpolateFloat(start: Float, end: Float, fraction: Float): Float {
    val clampedFraction = fraction.coerceIn(0f, 1f)
    return start + (end - start) * clampedFraction
}

private fun isTaskActive(task: TimeTask, now: LocalTime): Boolean {

    val start = task.startTotalMinutes()
    val end = task.normalizedEndTotalMinutes()
    val nowValue = nowMinute(now)
    val adjustedNow = if (end >= 24 * 60 && nowValue < start) nowValue + 24 * 60 else nowValue
    return adjustedNow in start until end
}

private fun minutesUntilTask(task: TimeTask, now: LocalTime): Int {
    val nowValue = nowMinute(now)
    val start = task.startTotalMinutes()
    return if (start >= nowValue) start - nowValue else 24 * 60 - nowValue + start
}

private fun TimeTask.normalizedEndTotalMinutes(): Int {
    val start = startTotalMinutes()
    val end = endTotalMinutes()
    return if (end <= start) end + 24 * 60 else end
}

private fun screenHeadline(
    screenMode: MainScreenMode,
    isNightMode: Boolean,
    isEditing: Boolean,
): String {
    return when (screenMode) {
        MainScreenMode.Normal -> if (isNightMode) "Manage your night" else "Manage your day"
        MainScreenMode.SetProject -> if (isEditing) "Edit your project" else "Set your project"
    }
}

private fun screenSupportText(
    screenMode: MainScreenMode,
    supportQuote: String,
    isEditing: Boolean,
): String {
    return when (screenMode) {
        MainScreenMode.Normal -> supportQuote
        MainScreenMode.SetProject -> if (isEditing) "Update title, color, and time." else "Set title, color, and time."
    }
}



private fun draftSummaryTitle(
    title: String,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
): String {
    val draftName = title.ifBlank { "Untitled project" }
    return "$draftName\n${formatHourMinute(startHour, startMinute)} - ${formatHourMinute(endHour, endMinute)}"
}

private fun buildDraftSummary(
    title: String,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
): String {
    val draftName = title.ifBlank { "Untitled project" }
    return "$draftName · ${formatHourMinute(startHour, startMinute)} - ${formatHourMinute(endHour, endMinute)}"
}



private fun currentTaskSubtitle(task: TimeTask, now: LocalTime): String {
    val remaining = max(remainingMinutes(task, now), 0)
    val hours = remaining / 60
    val minutes = remaining % 60
    return "${task.title} · ${task.prettyTimeRange()} · $hours hour(s) $minutes min(s) left"
}

private fun nextTaskSubtitle(task: TimeTask, now: LocalTime): String {
    val until = minutesUntilTask(task, now)
    val hours = until / 60
    val minutes = until % 60
    return "${task.title} · begins at ${formatHourMinute(task.startHour, task.startMinute)} · in $hours hour(s) $minutes min(s)"
}

private fun nowMinute(now: LocalTime): Int = now.hour * 60 + now.minute


private fun TimeTask.startTotalMinutes(): Int = startHour * 60 + startMinute

private fun TimeTask.endTotalMinutes(): Int = endHour * 60 + endMinute

private fun TimeTask.prettyTimeRange(): String =
    "${formatHourMinute(startHour, startMinute)} - ${formatHourMinute(endHour, endMinute)}"

private fun formatHourMinute(hour: Int, minute: Int): String =
    LocalTime.of(hour.coerceIn(0, 23), minute.coerceIn(0, 59)).format(timeFormatter)

private fun formatMinutesOfDay(totalMinutes: Int): String =
    formatHourMinute(totalMinutes / 60, totalMinutes % 60)

private fun Int.toTwoDigits(): String = toString().padStart(2, '0')

private fun parseHourMinute(value: String): Int {

    val parts = value.split(":")
    if (parts.size != 2) return 0
    val hour = parts[0].toIntOrNull()?.coerceIn(0, 23) ?: 0
    val minute = parts[1].toIntOrNull()?.coerceIn(0, 59) ?: 0
    return hour * 60 + minute
}



private fun encodeStoredTasks(tasks: List<TimeTask>): String {
    val payload = JSONArray()
    tasks.forEach { task ->
        payload.put(
            JSONObject().apply {
                put("id", task.id)
                put("title", task.title)
                put("date", task.date.toString())
                put("startHour", task.startHour)
                put("startMinute", task.startMinute)
                put("endHour", task.endHour)
                put("endMinute", task.endMinute)
                put("colorArgb", task.color.toArgb())
            },
        )
    }
    return payload.toString()
}

private fun decodeStoredTasks(raw: String): List<TimeTask> = runCatching {
    val payload = JSONArray(raw)
    buildList(payload.length()) {
        for (index in 0 until payload.length()) {
            val item = payload.optJSONObject(index) ?: continue
            val id = item.optString("id").ifBlank { "task-${System.currentTimeMillis()}-$index" }
            val title = item.optString("title").ifBlank { "Untitled project" }
            val date = item.optString("date").takeIf { it.isNotBlank() }?.let(LocalDate::parse) ?: LocalDate.now()
            add(
                TimeTask(
                    id = id,
                    title = title,
                    date = date,
                    startHour = item.optInt("startHour").coerceIn(0, 23),
                    startMinute = item.optInt("startMinute").coerceIn(0, 59),
                    endHour = item.optInt("endHour").coerceIn(0, 23),
                    endMinute = item.optInt("endMinute").coerceIn(0, 59),
                    color = Color(item.optInt("colorArgb", taskColorPalette.first().toArgb())),
                ),
            )
        }
    }
}.getOrElse { emptyList() }

private fun rangesOverlap(
    startA: Int,
    endA: Int,
    startB: Int,
    endB: Int,
): Boolean = startA < endB && startB < endA










