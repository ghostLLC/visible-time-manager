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



import androidx.compose.runtime.movableContentOf



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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke


import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
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
import com.vtm.app.ui.theme.ClockBlue
import com.vtm.app.ui.theme.ClockGreen
import com.vtm.app.ui.theme.ClockPurple
import com.vtm.app.ui.theme.ClockRed
import com.vtm.app.ui.theme.ClockTeal
import com.vtm.app.ui.theme.ClockYellow
import com.vtm.app.ui.theme.VTMTheme
import java.time.Duration
import java.time.LocalTime

import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.roundToInt


private enum class MainScreenMode {
    Normal,
    SetProject,
}

private val taskTypeOptions = listOf("Study", "Work", "Exercise", "Life", "Break")
private val taskColorPalette = listOf(
    ClockBlue,
    ClockPurple,
    ClockGreen,
    ClockYellow,
    ClockTeal,
    ClockRed,
    Color(0xFF5B8DEF),
    Color(0xFF8B5CF6),
    Color(0xFFF97316),
    Color(0xFF14B8A6),
)
private val taskTypeColors = mapOf(
    "Study" to ClockBlue,
    "Work" to ClockPurple,
    "Exercise" to ClockGreen,
    "Life" to ClockYellow,
    "Break" to ClockTeal,
)
private enum class BottomNavPage {
    Calendar,
    Clock,
    Profile,
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
        label = "Mine",
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

    var screenMode by rememberSaveable { mutableStateOf(MainScreenMode.Normal) }
    var tasks by remember {
        mutableStateOf(
            listOf(
                TimeTask(
                    id = "day-deep-work",
                    title = "Deep Work Block",
                    type = "Work",
                    startHour = 10,
                    startMinute = 0,
                    endHour = 14,
                    endMinute = 0,
                    color = taskTypeColors.getValue("Work"),
                ),
                TimeTask(
                    id = "evening-study",
                    title = "Evening Study Sprint",
                    type = "Study",
                    startHour = 16,
                    startMinute = 0,
                    endHour = 19,
                    endMinute = 0,
                    color = taskTypeColors.getValue("Study"),
                ),
                TimeTask(
                    id = "night-reset",
                    title = "Night Reset Session",
                    type = "Exercise",
                    startHour = 4,
                    startMinute = 0,
                    endHour = 9,
                    endMinute = 0,
                    color = taskTypeColors.getValue("Exercise"),
                ),
            ),
        )
    }
    var activeBottomPage by rememberSaveable { mutableStateOf(BottomNavPage.Clock) }


    var draftTitle by rememberSaveable { mutableStateOf("") }
    var draftType by rememberSaveable { mutableStateOf(taskTypeOptions.first()) }
    var typeMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var draftStartHour by rememberSaveable { mutableStateOf(8) }
    var draftStartMinute by rememberSaveable { mutableStateOf(0) }
    var draftEndHour by rememberSaveable { mutableStateOf(9) }
    var draftEndMinute by rememberSaveable { mutableStateOf(0) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var taskPendingDelete by remember { mutableStateOf<TimeTask?>(null) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var taskPendingEdit by remember { mutableStateOf<TimeTask?>(null) }
    var selectedClockTaskId by remember { mutableStateOf<String?>(null) }
    var pressedClockTaskId by remember { mutableStateOf<String?>(null) }
    var pinnedClockTaskId by remember { mutableStateOf<String?>(null) }
    var selectedClockEmptySlot by remember { mutableStateOf<ClockEmptySlot?>(null) }

    var selectedClockTaskBounceKey by remember { mutableIntStateOf(0) }
    var selectedClockEmptySlotBounceKey by remember { mutableIntStateOf(0) }
    var showSetActionsExpanded by rememberSaveable { mutableStateOf(false) }



    var editingTaskId by rememberSaveable { mutableStateOf<String?>(null) }

    val tasksExcludingEditing = remember(tasks, editingTaskId) {
        tasks.filterNot { it.id == editingTaskId }
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



    val sortedTasks = remember(tasks) { tasks.sortedBy { it.startTotalMinutes() } }

    val previewTasks = remember(



        sortedTasks,
        screenMode,
        editingTaskId,
        draftTitle,
        draftType,
        draftStartHour,
        draftStartMinute,
        draftEndHour,
        draftEndMinute,
        draftTimeError,
    ) {
        if (screenMode != MainScreenMode.SetProject || draftTimeError != null) {
            sortedTasks
        } else {
            val draftPreviewTask = TimeTask(
                id = editingTaskId ?: "draft-preview",
                title = draftTitle.ifBlank {
                    if (editingTaskId != null) "Edited ${draftType} Project" else "New ${draftType} Project"
                },
                type = draftType,
                startHour = draftStartHour,
                startMinute = draftStartMinute,
                endHour = draftEndHour,
                endMinute = draftEndMinute,
                color = taskTypeColor(draftType),
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
        pressedClockTaskId = null
        pinnedClockTaskId = null
        if (clearSelection) {
            selectedClockTaskId = null
        }
    }

    fun releaseClockTaskPeak() {
        if (pinnedClockTaskId != null) {
            return
        }
        pressedClockTaskId = null
    }





    fun beginEditing(task: TimeTask) {
        selectedClockTaskId = task.id
        pressedClockTaskId = null
        pinnedClockTaskId = task.id
        selectedClockEmptySlot = null
        showSetActionsExpanded = false
        editingTaskId = task.id
        draftTitle = task.title
        draftType = task.type
        draftStartHour = task.startHour
        draftStartMinute = task.startMinute
        draftEndHour = task.endHour
        draftEndMinute = task.endMinute
        typeMenuExpanded = false
        taskPendingEdit = null
        showEditDialog = false
        screenMode = MainScreenMode.SetProject
    }

    fun pinClockTaskPeak(task: TimeTask) {
        pressedClockTaskId = null
        pinnedClockTaskId = task.id
        selectedClockTaskId = task.id
        selectedClockEmptySlot = null
        selectedClockTaskBounceKey += 1
        beginEditing(task)
    }


    fun resetDraft() {
        editingTaskId = null
        selectedClockEmptySlot = null
        draftTitle = ""
        draftType = taskTypeOptions.first()
        draftStartHour = if (isNightMode) 19 else 8
        draftStartMinute = 0
        draftEndHour = if (isNightMode) 20 else 9
        draftEndMinute = 0
        typeMenuExpanded = false
    }

    fun returnToClockHome() {
        typeMenuExpanded = false
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

    val canHandleBack = showDeleteDialog || showEditDialog || showSetActionsExpanded || typeMenuExpanded || screenMode == MainScreenMode.SetProject







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

            typeMenuExpanded -> {
                typeMenuExpanded = false
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
        draftTitle = ""
        draftType = taskTypeOptions.first()
        val boundedStart = slot.startTotalMinutes.coerceIn(0, 30 * 60 - 1)
        val boundedEnd = slot.endTotalMinutes.coerceIn(boundedStart + 1, 30 * 60)
        applyDraftRange(
            startTotalMinutes = boundedStart,
            endTotalMinutes = boundedEnd,
        )
        typeMenuExpanded = false
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
        val title = draftTitle.ifBlank { "New ${draftType} Project" }
        val savedTask = TimeTask(
            id = editingTaskId ?: "task-${System.currentTimeMillis()}",
            title = title,
            type = draftType,
            startHour = draftStartHour,
            startMinute = draftStartMinute,
            endHour = draftEndHour,
            endMinute = draftEndMinute,
            color = taskTypeColor(draftType),
        )
        tasks = (tasksExcludingEditing + savedTask).sortedBy { it.startTotalMinutes() }
        selectedClockTaskId = savedTask.id
        pressedClockTaskId = null
        pinnedClockTaskId = savedTask.id
        selectedClockTaskBounceKey += 1
        selectedClockEmptySlot = null
        screenMode = MainScreenMode.Normal
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
            val activeCoinRotation = if (isCoinFlipSettling) {
                coinFlipRotation.value
            } else {
                coinFlipDragRotation
            }
            val coinRotation = activeCoinRotation
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
            val coinThickness = with(density) { (8.dp + 14.dp * absSin).toPx() }
            val rimAlpha = (0.22f + 0.54f * absSin).coerceIn(0f, 0.9f)
            val rimStrokePx = coinThickness
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
                                pressedClockTaskId = null
                                pinnedClockTaskId = null
                                selectedClockTaskId = null
                            }
                        },


                        onTaskDoubleTap = { tappedTask ->
                            selectedClockEmptySlot = null
                            pressedClockTaskId = null
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
            val pageVisibleTasks = remember(sortedTasks, displayNightMode) {
                tasksForMode(sortedTasks, displayNightMode)
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
            val primarySectionSpacing = if (isCompactWidth) 20.dp else 30.dp
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
                            NavigationBar {
                                bottomNavItems.forEach { item ->
                                    NavigationBarItem(
                                        selected = activeBottomPage == item.page,
                                        onClick = { activeBottomPage = item.page },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.label,
                                            )
                                        },
                                        alwaysShowLabel = false,
                                    )
                                }
                            }
                        }
                    }
                },
            ) { paddingValues ->



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = contentHorizontalPadding, vertical = contentVerticalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (screenMode == MainScreenMode.Normal && activeBottomPage == BottomNavPage.Clock) {
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

                        Spacer(modifier = Modifier.height(18.dp))
                    } else if (screenMode != MainScreenMode.Normal) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    if (screenMode == MainScreenMode.Normal && activeBottomPage != BottomNavPage.Clock) {
                        PlaceholderFeaturePage(
                            title = if (activeBottomPage == BottomNavPage.Calendar) "Calendar" else "Mine",
                            subtitle = if (activeBottomPage == BottomNavPage.Calendar) {
                                "Calendar page is reserved for now. We can fill in schedule and date capabilities later."
                            } else {
                                "Mine page is reserved for now. We can place profile and personal settings here later."
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = true),
                        )
                    } else {
                        if (useSimplifiedNormalPreview && screenMode == MainScreenMode.Normal) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                ProjectSummaryCard(
                                    sectionTitle = if (pageCurrentTask == null) "Current" else "Current",
                                    task = pageCurrentTask,
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                ProjectSummaryCard(
                                    sectionTitle = if (pageNextTask == null) "Next" else "Next",
                                    task = pageNextTask,
                                )
                            }
                        } else {
                            CoinFlipClock(
                                modifier = Modifier.fillMaxWidth(),
                                displayNightMode = displayNightMode,
                            )
                        }

                        Spacer(modifier = Modifier.height(primarySectionSpacing))


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
                                                        ProjectSummaryCard(
                                                            sectionTitle = if (pageCurrentTask == null) "Current" else "Current",
                                                            task = pageCurrentTask,
                                                        )

                                                        Spacer(modifier = Modifier.height(10.dp))

                                                        ProjectSummaryCard(
                                                            sectionTitle = if (pageNextTask == null) "Next" else "Next",
                                                            task = pageNextTask,
                                                        )





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
                                                selectedType = draftType,
                                                onToggleTypeMenu = { typeMenuExpanded = !typeMenuExpanded },
                                                typeMenuExpanded = typeMenuExpanded,
                                                onSelectType = {
                                                    draftType = it
                                                    typeMenuExpanded = false
                                                },
                                                startHour = draftStartHour,
                                                startMinute = draftStartMinute,
                                                endHour = draftEndHour,
                                                endMinute = draftEndMinute,
                                                earliestEndTotalMinutes = earliestDraftEndTotalMinutes,
                                                occupiedStartTimes = unavailableStartTimes(tasksExcludingEditing),
                                                occupiedEndTimes = unavailableEndTimes(tasksExcludingEditing, earliestDraftEndTotalMinutes),
                                                draftColor = previewDraftColor(draftType),
                                                timeError = draftTimeError,
                                                onSelectStartTime = { updateDraftStart(it) },
                                                onSelectEndTime = { updateDraftEnd(it) },
                                                onSave = { saveDraft() },
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
    }


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
                                        text = "${iconForType(task.type)} ${task.title}",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Text(
                                    text = "${task.type} · ${task.prettyTimeRange()}",
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
                                        text = "${iconForType(task.type)} ${task.title}",
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
    timeRange: String,
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















@Composable
private fun DraftProjectCard(
    isEditing: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    selectedType: String,
    onToggleTypeMenu: () -> Unit,
    typeMenuExpanded: Boolean,
    onSelectType: (String) -> Unit,
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
) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(12.dp)
                            .background(draftColor, CircleShape),
                    )
                    Text(
                        text = if (isEditing) "Edit project" else "Set project",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                    )
                }

                Box {
                    OutlinedButton(
                        onClick = onToggleTypeMenu,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, draftColor.copy(alpha = 0.35f)),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            containerColor = draftColor.copy(alpha = 0.08f),
                            contentColor = draftColor,
                        ),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(10.dp)
                                    .height(10.dp)
                                    .background(draftColor, CircleShape),
                            )
                            Text(
                                text = "Type · $selectedType",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = draftColor,
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = typeMenuExpanded,
                        onDismissRequest = { if (typeMenuExpanded) onToggleTypeMenu() },
                    ) {
                        taskTypeOptions.forEach { option ->
                            val optionColor = taskTypeColor(option)
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .width(10.dp)
                                                .height(10.dp)
                                                .background(optionColor, CircleShape),
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(option)
                                    }
                                },
                                onClick = { onSelectType(option) },
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
            Spacer(modifier = Modifier.height(12.dp))
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






@Composable
private fun WheelColumn(
    label: String,
    values: List<Int>,
    selectedValue: Int,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit,
) {
    val selectedIndex = values.indexOf(selectedValue).coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = max(0, selectedIndex - 2))

    LaunchedEffect(selectedValue) {
        val target = max(0, values.indexOf(selectedValue) - 2)
        listState.scrollToItem(target)
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp),
            ) {
                items(values.size) { index ->
                    val option = values[index]
                    val isSelected = option == selectedValue
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelected(option) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = option.toTwoDigits(),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = if (isSelected) 22.sp else 18.sp,
                        )
                    }
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
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(12.dp)
                        .background((task?.color ?: MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)), CircleShape),
                )
                Text(
                    text = if (task == null) sectionTitle else "$sectionTitle: ${iconForType(task.type)} ${task.title}",
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 22.dp),
                )
            }
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
            label = "时",
            values = enabledHours,
            selectedValue = effectiveHour,
            modifier = Modifier.weight(1f),
            isDarkMode = isDarkMode,
            onSelected = onHourSelected,
        )
        SnapWheelColumn(
            label = "分",
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

private fun taskTypeColor(type: String): Color = taskTypeColors[type] ?: ClockBlue

private fun previewDraftColor(
    draftType: String,
): Color = taskTypeColor(draftType)

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
                                    text = "${iconForType(task.type)} ${task.type} · ${task.prettyTimeRange()} · ${taskWindowLabel(task)}",
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
        MainScreenMode.SetProject -> if (isEditing) "Update title, type, and time." else "Set title, type, and time."
    }
}



private fun draftSummaryTitle(
    title: String,
    type: String,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
): String {
    val draftName = title.ifBlank { "Untitled $type block" }
    return "$draftName\n${formatHourMinute(startHour, startMinute)} - ${formatHourMinute(endHour, endMinute)}"
}

private fun buildDraftSummary(
    title: String,
    type: String,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
): String {
    val draftName = title.ifBlank { "Untitled $type block" }
    return "$draftName · ${iconForType(type)} $type · ${formatHourMinute(startHour, startMinute)} - ${formatHourMinute(endHour, endMinute)}"
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

private fun iconForType(type: String?): String {
    return when (type) {
        "Study" -> "✏️"
        "Work" -> "💼"
        "Exercise" -> "🏃"
        "Life" -> "🌙"
        "Break" -> "☕"
        else -> "🕒"
    }
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



private fun rangesOverlap(
    startA: Int,
    endA: Int,
    startB: Int,
    endB: Int,
): Boolean = startA < endB && startB < endA



