package com.vtm.app.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
                    id = "focus-study",
                    title = "Morning Review",
                    type = "Study",
                    startHour = 9,
                    startMinute = 0,
                    endHour = 10,
                    endMinute = 30,
                    color = taskTypeColors.getValue("Study"),
                ),
                TimeTask(
                    id = "project-work",
                    title = "Visible Time Prototype",
                    type = "Work",
                    startHour = 14,
                    startMinute = 0,
                    endHour = 16,
                    endMinute = 0,
                    color = taskTypeColors.getValue("Work"),
                ),
                TimeTask(
                    id = "night-reset",
                    title = "Evening Walk",
                    type = "Life",
                    startHour = 20,
                    startMinute = 30,
                    endHour = 21,
                    endMinute = 30,
                    color = taskTypeColors.getValue("Life"),
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



    val draftStartTotalMinutes = draftStartHour * 60 + draftStartMinute
    val draftEndTotalMinutes = draftEndHour * 60 + draftEndMinute
    val earliestDraftEndTotalMinutes = (draftStartTotalMinutes + 1).coerceAtMost(23 * 60 + 59)
    val draftOverlapTask = remember(tasks, draftStartTotalMinutes, draftEndTotalMinutes) {
        tasks.firstOrNull {
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
                id = "draft-preview",
                title = draftTitle.ifBlank { "New ${draftType} Project" },
                type = draftType,
                startHour = draftStartHour,
                startMinute = draftStartMinute,
                endHour = draftEndHour,
                endMinute = draftEndMinute,
                color = previewDraftColor(sortedTasks, draftType),
            )
            (sortedTasks + draftPreviewTask).sortedBy { it.startTotalMinutes() }
        }
    }






    val pagerState = rememberPagerState(pageCount = { 2 })
    val primaryActionLabel = when (screenMode) {
        MainScreenMode.Normal -> "SET"
        MainScreenMode.Set -> "Back"
        MainScreenMode.SetProject -> "Back"
    }

    fun resetDraft() {
        draftTitle = ""
        draftType = taskTypeOptions.first()
        draftStartHour = if (isNightMode) 19 else 8
        draftStartMinute = 0
        draftEndHour = if (isNightMode) 20 else 9
        draftEndMinute = 0
        typeMenuExpanded = false
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
        val newTask = TimeTask(
            id = "task-${System.currentTimeMillis()}",
            title = title,
            type = draftType,
            startHour = draftStartHour,
            startMinute = draftStartMinute,
            endHour = draftEndHour,
            endMinute = draftEndMinute,
            color = nextAvailableTaskColor(tasks, preferred = taskTypeColors[draftType] ?: ClockBlue),
        )
        tasks = (tasks + newTask).sortedBy { it.startTotalMinutes() }
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
            Text(
                text = screenHeadline(screenMode = screenMode, isNightMode = isNightMode),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = screenSupportText(screenMode = screenMode, isNightMode = isNightMode),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(18.dp))

            Crossfade(
                targetState = isNightMode,
                animationSpec = tween(durationMillis = 280),
                modifier = Modifier.fillMaxWidth(),
                label = "mode-clock-crossfade",
            ) { animatedNightMode ->
                ClockFace(
                    isNightMode = animatedNightMode,
                    tasks = previewTasks,
                    centerTitle = "",
                    centerSubtitle = "",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
            ) {
                when (screenMode) {
                    MainScreenMode.Normal -> {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            userScrollEnabled = visibleTasks.isNotEmpty(),
                        ) { page ->
                            when (page) {
                                0 -> {
                                    Column(modifier = Modifier.fillMaxWidth()) {
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
                                    TimelinePreview(tasks = timelineTasksForMode(sortedTasks, isNightMode))
                                }
                            }
                        }
                    }

                    MainScreenMode.Set -> {
                        SetStageCard(
                            hasTasks = tasks.isNotEmpty(),
                            onProceed = {
                                resetDraft()
                                screenMode = MainScreenMode.SetProject
                            },
                            onDeleteProject = {
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
                                occupiedStartTimes = unavailableStartTimes(tasks),
                                occupiedEndTimes = unavailableEndTimes(tasks, earliestDraftEndTotalMinutes),
                                draftColor = previewDraftColor(tasks, draftType),
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
        }
    }


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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompactModeChip(
            text = "Day",
            selected = !isNightMode,
            onClick = { onNightModeChange(false) },
        )
        CompactModeChip(
            text = "Night",
            selected = isNightMode,
            onClick = { onNightModeChange(true) },
        )
    }
}

@Composable
private fun CompactModeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 0.dp),
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.24f),
        ),
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 14.sp,
        )
    }
}







@Composable
private fun DraftProjectCard(
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
                .padding(horizontal = 18.dp, vertical = 20.dp),
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
                    text = "Set project",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
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
            Box {
                OutlinedButton(
                    onClick = onToggleTypeMenu,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.24f)),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(12.dp)
                                .background(draftColor, CircleShape),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Type · $selectedType")
                    }
                }
                DropdownMenu(
                    expanded = typeMenuExpanded,
                    onDismissRequest = onToggleTypeMenu,
                ) {
                    taskTypeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(10.dp)
                                            .background(previewDraftColor(emptyList(), option), CircleShape),
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
            Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = timeError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
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
                    Text("Save project")
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
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
    var selectedTotalMinutes by remember(initialTotalMinutes) {
        mutableStateOf(
            nearestSelectableTime(
                preferred = initialTotalMinutes,
                minTotalMinutes = minTotalMinutes,
                occupiedTimes = occupiedTimes,
            ),
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    nearestSelectableTime(
                        preferred = selectedTotalMinutes,
                        minTotalMinutes = minTotalMinutes,
                        occupiedTimes = occupiedTimes,
                    ),
                )
            }) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(
                text = "Select time",
                fontWeight = FontWeight.SemiBold,
            )
        },
        text = {
            SnapTimePicker(
                selectedTotalMinutes = selectedTotalMinutes,
                minTotalMinutes = minTotalMinutes,
                occupiedTimes = occupiedTimes,
                onSelected = { selectedTotalMinutes = it },
            )
        },
        shape = RoundedCornerShape(24.dp),
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
    onProceed: () -> Unit,
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
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onProceed,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Text("Set Project")
                }
                OutlinedButton(
                    onClick = onDeleteProject,
                    enabled = hasTasks,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                ) {
                    Text("Delete Project")
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
                    color = MaterialTheme.colorScheme.onSurface,
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
    selectedTotalMinutes: Int,
    minTotalMinutes: Int,
    occupiedTimes: Set<Int>,
    onSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SnapWheelColumn(
            label = "Hour",
            values = (0..23).toList(),
            selectedValue = selectedTotalMinutes / 60,
            modifier = Modifier.weight(1f),
            isValueEnabled = { hour ->
                selectableMinutesForHour(hour, minTotalMinutes, occupiedTimes).isNotEmpty()
            },
            onSelected = { hour ->
                val target = nearestSelectableForHour(
                    hour = hour,
                    currentMinute = selectedTotalMinutes % 60,
                    minTotalMinutes = minTotalMinutes,
                    occupiedTimes = occupiedTimes,
                )
                onSelected(target)
            },
        )
        SnapWheelColumn(
            label = "Minute",
            values = (0..59).toList(),
            selectedValue = selectedTotalMinutes % 60,
            modifier = Modifier.weight(1f),
            isValueEnabled = { minute ->
                val candidate = (selectedTotalMinutes / 60) * 60 + minute
                candidate >= minTotalMinutes && candidate !in occupiedTimes
            },
            onSelected = { minute ->
                val candidate = (selectedTotalMinutes / 60) * 60 + minute
                onSelected(
                    nearestSelectableTime(
                        preferred = candidate,
                        minTotalMinutes = minTotalMinutes,
                        occupiedTimes = occupiedTimes,
                    ),
                )
            },
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
    isValueEnabled: (Int) -> Boolean = { true },
    onSelected: (Int) -> Unit,
) {
    val selectedIndex = values.indexOf(selectedValue).coerceAtLeast(0)
    val rowHeight = 44.dp
    val rowHeightPx = with(androidx.compose.ui.platform.LocalDensity.current) { rowHeight.roundToPx() }
    val centerSlots = 2
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = max(0, selectedIndex - centerSlots))
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    fun nearestResolvedValue(index: Int): Int {
        val candidate = values.getOrNull(index) ?: values.last()
        return if (isValueEnabled(candidate)) candidate else nearestEnabledValue(values, candidate, isValueEnabled)
    }

    LaunchedEffect(values, selectedValue) {
        val target = max(0, values.indexOf(selectedValue) - centerSlots)
        if (listState.firstVisibleItemIndex != target || listState.firstVisibleItemScrollOffset != 0) {
            listState.scrollToItem(target)
        }
    }

    LaunchedEffect(listState, values, selectedValue) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling && values.isNotEmpty()) {
                    val offsetIndexDelta = ((listState.firstVisibleItemScrollOffset.toFloat() / rowHeightPx).let { if (it >= 0.5f) 1 else 0 })
                    val snappedIndex = (listState.firstVisibleItemIndex + centerSlots + offsetIndexDelta)
                        .coerceIn(0, values.lastIndex)
                    val resolved = nearestResolvedValue(snappedIndex)
                    if (resolved != selectedValue) {
                        onSelected(resolved)
                    }
                    val targetIndex = max(0, values.indexOf(resolved) - centerSlots)
                    val targetOffset = 0
                    if (listState.firstVisibleItemIndex != targetIndex || listState.firstVisibleItemScrollOffset != targetOffset) {
                        listState.animateScrollToItem(targetIndex, targetOffset)
                    }
                }
            }
    }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
            ) {
                LazyColumn(
                    state = listState,
                    flingBehavior = flingBehavior,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(rowHeight * 5),
                ) {
                    items(values.size) { index ->
                        val option = values[index]
                        val isSelected = option == selectedValue
                        val isEnabled = isValueEnabled(option)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(rowHeight)
                                .clickable(enabled = isEnabled) { onSelected(option) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = option.toTwoDigits(),
                                color = when {
                                    !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f)
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                                },
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = if (isSelected) 22.sp else 18.sp,
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(rowHeight)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(14.dp)),
            )
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

private fun previewDraftColor(
    tasks: List<TimeTask>,
    draftType: String,
): Color = nextAvailableTaskColor(tasks, taskTypeColors[draftType] ?: ClockBlue)

private fun nextAvailableTaskColor(
    tasks: List<TimeTask>,
    preferred: Color,
): Color {
    val usedColors = tasks.map { it.color }.toSet()
    if (preferred !in usedColors) return preferred
    return taskColorPalette.firstOrNull { it !in usedColors } ?: preferred
}



@Composable
private fun TimelinePreview(tasks: List<TimeTask>) {
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
        ) {
            Text(
                text = "Today timeline",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            tasks.forEach { task ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
                            text = "${iconForType(task.type)} ${task.type} · ${task.prettyTimeRange()}",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                            fontSize = 13.sp,
                        )
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

private fun timelineTasksForMode(
    tasks: List<TimeTask>,
    isNightMode: Boolean,
): List<TimeTask> = tasksForMode(tasks, isNightMode)



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
): String {
    return when (screenMode) {
        MainScreenMode.Normal -> if (isNightMode) "Manage your night" else "Manage your day"
        MainScreenMode.Set -> "Shape the visible plan"
        MainScreenMode.SetProject -> "Set your project"
    }
}

private fun screenSupportText(
    screenMode: MainScreenMode,
    isNightMode: Boolean,
): String {
    return when (screenMode) {
        MainScreenMode.Normal -> if (isNightMode) {
            "Night focus, clean clock, fewer distractions."
        } else {
            "Day focus, clear clock, ready for the next block."
        }
        MainScreenMode.Set -> "Choose the next project slot."
        MainScreenMode.SetProject -> "Set title, type, and time."
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



