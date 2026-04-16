package com.vtm.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith


import androidx.compose.foundation.BorderStroke


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.layout.Box




import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue



import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow


import androidx.compose.ui.unit.dp

import androidx.compose.ui.unit.sp

import com.vtm.app.components.ClockFace
import com.vtm.app.model.TimeTask
import com.vtm.app.ui.theme.ClockBlue
import com.vtm.app.ui.theme.ClockGreen
import com.vtm.app.ui.theme.ClockPurple
import com.vtm.app.ui.theme.ClockRed
import com.vtm.app.ui.theme.ClockTeal
import com.vtm.app.ui.theme.ClockYellow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

private enum class MainScreenMode {
    Normal,
    Set,
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
private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

private fun MainScreenMode.transitionLevel(): Int = when (this) {
    MainScreenMode.Normal -> 0
    MainScreenMode.Set -> 1
    MainScreenMode.SetProject -> 2
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


@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(

    isNightMode: Boolean,
    onNightModeChange: (Boolean) -> Unit,
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
    var editingTaskId by rememberSaveable { mutableStateOf<String?>(null) }

    val editingTask = remember(tasks, editingTaskId) {
        tasks.firstOrNull { it.id == editingTaskId }
    }
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


    val now = remember(tasks, isNightMode, screenMode, draftTitle, draftType, draftStartHour, draftStartMinute, draftEndHour, draftEndMinute) {
        LocalTime.now()
    }
    val sortedTasks = remember(tasks) { tasks.sortedBy { it.startTotalMinutes() } }
    val visibleTasks = remember(sortedTasks, isNightMode) {
        tasksForMode(sortedTasks, isNightMode)
    }
    val currentTask = remember(visibleTasks, now) {
        visibleTasks.firstOrNull { task -> isTaskActive(task, now) }
    }
    val nextTask = remember(visibleTasks, currentTask, now) {
        when {
            visibleTasks.isEmpty() -> null
            currentTask != null -> {
                val currentIndex = visibleTasks.indexOfFirst { it.id == currentTask.id }
                if (currentIndex >= 0 && currentIndex < visibleTasks.lastIndex) {
                    visibleTasks[currentIndex + 1]
                } else {
                    visibleTasks.firstOrNull { it.id != currentTask.id }
                }
            }
            else -> visibleTasks.minByOrNull { task -> minutesUntilTask(task, now) }
        }
    }

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








    val primaryActionLabel = when (screenMode) {

        MainScreenMode.Normal -> "SET"
        MainScreenMode.Set -> "Back"
        MainScreenMode.SetProject -> "Back"
    }

    fun resetDraft() {
        editingTaskId = null
        draftTitle = ""
        draftType = taskTypeOptions.first()
        draftStartHour = if (isNightMode) 19 else 8
        draftStartMinute = 0
        draftEndHour = if (isNightMode) 20 else 9
        draftEndMinute = 0
        typeMenuExpanded = false
    }

    fun beginEditing(task: TimeTask) {
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
        screenMode = MainScreenMode.Normal
        resetDraft()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Button(
                onClick = {
                    when (screenMode) {
                        MainScreenMode.Normal -> screenMode = MainScreenMode.Set
                        MainScreenMode.Set -> screenMode = MainScreenMode.Normal
                        MainScreenMode.SetProject -> {
                            typeMenuExpanded = false
                            screenMode = MainScreenMode.Set
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                shape = RoundedCornerShape(18.dp),
            ) {
                Text(
                    text = primaryActionLabel,
                    modifier = Modifier.padding(vertical = 6.dp),
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (screenMode == MainScreenMode.Normal) {
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
                        maxLines = 1,
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
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            ClockFace(
                isNightMode = isNightMode,
                tasks = previewTasks,
                centerTitle = "",
                centerSubtitle = "",
                modifier = Modifier.fillMaxWidth(),
            )


            Spacer(modifier = Modifier.height(30.dp))


            if (screenMode != MainScreenMode.SetProject) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ModeToggleRow(
                        isNightMode = isNightMode,
                        onNightModeChange = onNightModeChange,
                    )

                    if (screenMode == MainScreenMode.Normal && visibleTasks.isNotEmpty()) {
                        PagerIndicatorRow(
                            currentPage = pagerState.currentPage,
                            pageCount = 2,
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

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
                            userScrollEnabled = visibleTasks.isNotEmpty(),
                            verticalAlignment = Alignment.Top,
                        ) { page ->
                            when (page) {
                                0 -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxSize(),
                                    ) {
                                        ProjectSummaryCard(
                                            sectionTitle = if (currentTask == null) "No Project Currently" else "Current Project",
                                            task = currentTask,
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        ProjectSummaryCard(
                                            sectionTitle = if (nextTask == null) "No Next Project" else "Next Project",
                                            task = nextTask,
                                        )
                                    }
                                }

                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.TopStart,
                                    ) {
                                        TimelinePreview(
                                            tasks = sortedTasks,
                                            modifier = Modifier.fillMaxSize(),
                                        )

                                    }
                                }
                            }
                        }
                    }

                    MainScreenMode.Set -> {
                        SetStageCard(
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
                        screenMode = MainScreenMode.Set
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
                            border = BorderStroke(
                                1.dp,
                                if (selected) task.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.24f),
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
            ) {
                Text("Edit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
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
                            border = BorderStroke(
                                1.dp,
                                if (selected) task.color else MaterialTheme.colorScheme.outline.copy(alpha = 0.24f),
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
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
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
    text: String,
    timeRange: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 5.dp),
        elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(
            defaultElevation = if (selected) 2.dp else 0.dp,
            pressedElevation = if (selected) 3.dp else 0.dp,
            focusedElevation = if (selected) 2.dp else 0.dp,
            hoveredElevation = if (selected) 2.dp else 0.dp,
        ),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.14f)
            },
            contentColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        ),
        border = BorderStroke(
            1.dp,
            if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.62f)
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)
            },
        ),
    ) {
        Column(
            modifier = Modifier.width(54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 14.sp,
            )

            Text(
                text = timeRange,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.42f)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.30f)
                },
                fontWeight = FontWeight.Medium,
                fontSize = 8.sp,
            )
        }
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
        Color(0xFF1A1B1F).copy(alpha = 0.96f)
    } else {
        Color(0xFFF8F9FD).copy(alpha = 0.98f)
    }
    val titleColor = if (isDarkMode) Color.White.copy(alpha = 0.96f) else Color(0xFF111827)
    val subtitleColor = if (isDarkMode) Color.White.copy(alpha = 0.92f) else Color(0xFF4B5563)
    val secondaryButtonColor = if (isDarkMode) {
        Color.White.copy(alpha = 0.10f)
    } else {
        Color(0xFFEEF2F7)
    }
    val secondaryButtonContentColor = if (isDarkMode) {
        Color.White.copy(alpha = 0.88f)
    } else {
        Color(0xFF374151)
    }


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
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = secondaryButtonColor,
                            contentColor = secondaryButtonContentColor,
                        ),
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold)
                    }
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
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(18.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6),
                            contentColor = Color.White,
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
private fun SetStageCard(

    hasTasks: Boolean,
    onNewProject: () -> Unit,
    onEditProject: () -> Unit,
    onDeleteProject: () -> Unit,
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Shape your visible plan",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = onNewProject,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Text("New")
                }
                OutlinedButton(
                    onClick = onEditProject,
                    enabled = hasTasks,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Text("Edit")
                }
                OutlinedButton(
                    onClick = onDeleteProject,
                    enabled = hasTasks,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Text("Delete")
                }
            }
            if (!hasTasks) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "No saved projects yet.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                )
            }
        }
    }
}




@Composable
private fun PagerIndicatorRow(
    currentPage: Int,
    pageCount: Int,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(8.dp)
                    .background(
                        if (index == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.26f),
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
                    maxLines = 1,
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
            .filter { scrolling -> !scrolling }
            .debounce(72)
            .collectLatest {
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
        MainScreenMode.Set -> "Shape the visible plan"
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
        MainScreenMode.Set -> "Choose the next project slot."
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



