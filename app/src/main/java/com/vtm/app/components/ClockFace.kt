package com.vtm.app.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtm.app.model.TimeTask
import com.vtm.app.ui.theme.ClockCircleDark
import com.vtm.app.ui.theme.ClockCircleLight
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

enum class ClockModeTransitionStyle {
    Morph,
    Pulse,
    None,
}

enum class ClockFaceRenderQuality {
    Full,
    Reduced,
}

data class ClockEmptySlot(
    val startTotalMinutes: Int,
    val endTotalMinutes: Int,
)



@Composable
fun ClockFace(
    isNightMode: Boolean,
    tasks: List<TimeTask>,
    currentTime: LocalTime,
    modifier: Modifier = Modifier,
    modeTransitionStyle: ClockModeTransitionStyle = ClockModeTransitionStyle.Morph,
    pulseTriggerKey: Int = 0,
    renderQuality: ClockFaceRenderQuality = ClockFaceRenderQuality.Full,
    externalTransitionProgress: Float? = null,
    selectedTaskId: String? = null,
    pinnedPeakTaskId: String? = null,
    selectedEmptySlot: ClockEmptySlot? = null,
    selectedTaskBounceKey: Int = 0,
    selectedEmptySlotBounceKey: Int = 0,
    onTaskPress: ((TimeTask) -> Unit)? = null,
    onTaskRelease: ((TimeTask) -> Unit)? = null,
    onTaskLongPress: ((TimeTask) -> Unit)? = null,
    onTaskSingleTap: ((TimeTask) -> Unit)? = null,
    onTaskDoubleTap: ((TimeTask) -> Unit)? = null,
    onEmptyPress: ((ClockEmptySlot) -> Unit)? = null,
    onEmptyRelease: ((ClockEmptySlot) -> Unit)? = null,
    onEmptySingleTap: ((ClockEmptySlot) -> Unit)? = null,
    onEmptyDoubleTap: ((ClockEmptySlot) -> Unit)? = null,
) {
    val longPressTimeoutMillis = 900L
    val cachedTaskSegments = remember(tasks) {
        tasks.associateWith { task ->
            mapOf(
                ClockWindow.Day to segmentsForWindow(task, ClockWindow.Day),
                ClockWindow.Night to segmentsForWindow(task, ClockWindow.Night),
            )
        }
    }
    val morphProgress by animateFloatAsState(
        targetValue = if (isNightMode) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = Spring.StiffnessLow,
        ),
        label = "clock-mode-progress",
    )
    val pulseTargetState = remember { mutableFloatStateOf(0f) }
    val selectedTaskBounce = remember { Animatable(0f) }
    val selectedTaskBounceProgress = selectedTaskBounce.value
    val transientPressedTaskId = remember { mutableStateOf<String?>(null) }
    val visualPressedTaskId = remember { mutableStateOf<String?>(null) }
    val taskPressHold = remember { Animatable(0f) }
    val selectedEmptySlotBounce = remember { Animatable(0f) }
    val selectedEmptySlotBounceProgress = selectedEmptySlotBounce.value
    val pulseProgress by animateFloatAsState(
        targetValue = pulseTargetState.floatValue,
        animationSpec = spring(
            dampingRatio = 0.44f,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "clock-mode-pulse",
    )
    LaunchedEffect(modeTransitionStyle, pulseTriggerKey) {
        if (modeTransitionStyle == ClockModeTransitionStyle.Pulse) {
            pulseTargetState.floatValue = 1f
            withFrameNanos { }
            pulseTargetState.floatValue = 0f
        } else {
            pulseTargetState.floatValue = 0f
        }
    }
    LaunchedEffect(selectedTaskId, selectedTaskBounceKey, pinnedPeakTaskId) {
        val canRunSelectedBounce =
            selectedTaskId != null &&
                selectedTaskBounceKey != 0 &&
                pinnedPeakTaskId == null
        if (!canRunSelectedBounce) {
            selectedTaskBounce.snapTo(0f)
        } else {
            selectedTaskBounce.stop()
            selectedTaskBounce.snapTo(0f)
            withFrameNanos { }
            selectedTaskBounce.animateTo(
                targetValue = 0.48f,
                animationSpec = spring(
                    dampingRatio = 0.82f,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            )
            selectedTaskBounce.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.92f,
                    stiffness = Spring.StiffnessHigh,
                ),
            )
        }
    }
    LaunchedEffect(pinnedPeakTaskId, transientPressedTaskId.value) {
        when {
            pinnedPeakTaskId != null -> {
                transientPressedTaskId.value = null
                visualPressedTaskId.value = null
                taskPressHold.stop()
                if (taskPressHold.value < 0.73f) {
                    taskPressHold.animateTo(
                        targetValue = 0.74f,
                        animationSpec = spring(
                            dampingRatio = 0.82f,
                            stiffness = Spring.StiffnessMedium,
                        ),
                    )
                } else {
                    taskPressHold.snapTo(0.74f)
                }
            }
            transientPressedTaskId.value != null -> {
                visualPressedTaskId.value = transientPressedTaskId.value
                taskPressHold.stop()
                if (taskPressHold.value < 0.18f) {
                    taskPressHold.snapTo(0.18f)
                }
                taskPressHold.animateTo(
                    targetValue = 0.68f,
                    animationSpec = spring(
                        dampingRatio = 0.86f,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                )
            }
            else -> {
                taskPressHold.stop()
                if (taskPressHold.value <= 0.001f) {
                    taskPressHold.snapTo(0f)
                    visualPressedTaskId.value = null
                } else {
                    taskPressHold.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = 0.9f,
                            stiffness = Spring.StiffnessLow,
                        ),
                    )
                    visualPressedTaskId.value = null
                }
            }
        }
    }
    LaunchedEffect(selectedEmptySlot, selectedEmptySlotBounceKey) {
        if (selectedEmptySlot == null || selectedEmptySlotBounceKey == 0) {
            selectedEmptySlotBounce.snapTo(0f)
        } else {
            selectedEmptySlotBounce.stop()
            selectedEmptySlotBounce.snapTo(0f)
            withFrameNanos { }
            selectedEmptySlotBounce.animateTo(
                targetValue = 0.72f,
                animationSpec = spring(
                    dampingRatio = 0.72f,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            )
            selectedEmptySlotBounce.animateTo(
                targetValue = 0.18f,
                animationSpec = spring(
                    dampingRatio = 0.84f,
                    stiffness = Spring.StiffnessMedium,
                ),
            )
            selectedEmptySlotBounce.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = 0.92f,
                    stiffness = Spring.StiffnessHigh,
                ),
            )
        }
    }
    val transitionProgress = when {
        externalTransitionProgress != null -> externalTransitionProgress.coerceIn(0f, 1f)
        modeTransitionStyle == ClockModeTransitionStyle.Morph -> morphProgress
        else -> if (isNightMode) 1f else 0f
    }
    val clampedProgress = transitionProgress.coerceIn(0f, 1f)
    val overshootLift = if (modeTransitionStyle == ClockModeTransitionStyle.Morph && externalTransitionProgress == null) {
        (transitionProgress - clampedProgress) * 0.9f
    } else {
        0f
    }
    val pulse = when (modeTransitionStyle) {
        ClockModeTransitionStyle.Morph -> {
            if (externalTransitionProgress != null) {
                sin((clampedProgress * Math.PI).toFloat()) * 0.2f
            } else {
                sin((clampedProgress * Math.PI).toFloat())
            }
        }
        ClockModeTransitionStyle.Pulse -> sin((pulseProgress.coerceIn(0f, 1f) * Math.PI).toFloat()) * 0.72f
        ClockModeTransitionStyle.None -> 0f
    }
    val sweepDrift = if (modeTransitionStyle == ClockModeTransitionStyle.Morph) {
        (overshootLift + pulse * 0.12f) * 8f
    } else {
        0f
    }

    val circleColor = lerp(ClockCircleLight, ClockCircleDark, clampedProgress)
    val colorScheme = MaterialTheme.colorScheme
    val isDarkMode = colorScheme.surface.luminance() < 0.5f
    val controlFillColor = if (isDarkMode) {
        colorScheme.surfaceVariant.copy(alpha = 0.92f)
    } else {
        colorScheme.surfaceVariant
    }
    val controlOutlineColor = if (isDarkMode) {
        colorScheme.outline.copy(alpha = 0.5f)
    } else {
        colorScheme.outline.copy(alpha = 0.34f)
    }
    val hourLabelColor = lerp(controlFillColor, colorScheme.onSurface, if (isDarkMode) 0.54f else 0.46f)
    val hourTickColor = lerp(controlOutlineColor, colorScheme.onSurface, if (isDarkMode) 0.18f else 0.12f)
    val handColor = lerp(controlFillColor, colorScheme.onSurface, if (isDarkMode) 0.72f else 0.64f)
    val density = LocalDensity.current
    val paddingPx = with(density) { 12.dp.toPx() }
    val framePaddingPx = with(density) { 28.dp.toPx() }
    val outerStrokePx = with(density) { 42.dp.toPx() }
    val clockGestureModifier = if (
        onTaskPress != null ||
        onTaskSingleTap != null ||
        onTaskDoubleTap != null ||
        onEmptyPress != null ||
        onEmptySingleTap != null ||
        onEmptyDoubleTap != null
    ) {
        Modifier.pointerInput(
            tasks,
            isNightMode,
            onTaskPress,
            onTaskRelease,
            onTaskLongPress,
            onTaskSingleTap,
            onTaskDoubleTap,
            onEmptyPress,
            onEmptyRelease,
            onEmptySingleTap,
            onEmptyDoubleTap,
            paddingPx,
            framePaddingPx,
            outerStrokePx,
        ) {
            detectTapGestures(
                onPress = { pressOffset ->
                    val pressedTask = hitTestClockTask(
                        tapOffset = pressOffset,
                        canvasSize = size,
                        tasks = tasks,
                        isNightMode = isNightMode,
                        contentPaddingPx = paddingPx,
                        framePaddingPx = framePaddingPx,
                        outerStrokePx = outerStrokePx,
                    )
                    if (pressedTask != null) {
                        transientPressedTaskId.value = pressedTask.id
                        onTaskPress?.invoke(pressedTask)
                        var longPressTriggered = false
                        coroutineScope {
                            val longPressJob = launch {
                                delay(longPressTimeoutMillis)
                                longPressTriggered = true
                                transientPressedTaskId.value = null
                                onTaskLongPress?.invoke(pressedTask)
                            }
                            val released = tryAwaitRelease()
                            longPressJob.cancel()
                            transientPressedTaskId.value = null
                            if (!released || !longPressTriggered) {
                                onTaskRelease?.invoke(pressedTask)
                            }
                        }
                    } else {
                        val pressedEmptySlot = hitTestClockEmptySlot(
                            tapOffset = pressOffset,
                            canvasSize = size,
                            tasks = tasks,
                            isNightMode = isNightMode,
                            contentPaddingPx = paddingPx,
                            framePaddingPx = framePaddingPx,
                            outerStrokePx = outerStrokePx,
                        )
                        if (pressedEmptySlot != null) {
                            transientPressedTaskId.value = null
                            onEmptyPress?.invoke(pressedEmptySlot)
                            val released = tryAwaitRelease()
                            if (!released) {
                                onEmptyRelease?.invoke(pressedEmptySlot)
                            }
                        } else {
                            transientPressedTaskId.value = null
                            tryAwaitRelease()
                        }
                    }
                },
                onTap = { tapOffset ->
                    transientPressedTaskId.value = null
                    val tappedTask = hitTestClockTask(
                        tapOffset = tapOffset,
                        canvasSize = size,
                        tasks = tasks,
                        isNightMode = isNightMode,
                        contentPaddingPx = paddingPx,
                        framePaddingPx = framePaddingPx,
                        outerStrokePx = outerStrokePx,
                    )
                    if (tappedTask != null) {
                        onTaskSingleTap?.invoke(tappedTask)
                    } else {
                        hitTestClockEmptySlot(
                            tapOffset = tapOffset,
                            canvasSize = size,
                            tasks = tasks,
                            isNightMode = isNightMode,
                            contentPaddingPx = paddingPx,
                            framePaddingPx = framePaddingPx,
                            outerStrokePx = outerStrokePx,
                        )?.let { onEmptySingleTap?.invoke(it) }
                    }
                },
                onDoubleTap = { tapOffset ->
                    transientPressedTaskId.value = null
                    val tappedTask = hitTestClockTask(
                        tapOffset = tapOffset,
                        canvasSize = size,
                        tasks = tasks,
                        isNightMode = isNightMode,
                        contentPaddingPx = paddingPx,
                        framePaddingPx = framePaddingPx,
                        outerStrokePx = outerStrokePx,
                    )
                    if (tappedTask != null) {
                        onTaskDoubleTap?.invoke(tappedTask)
                    } else {
                        hitTestClockEmptySlot(
                            tapOffset = tapOffset,
                            canvasSize = size,
                            tasks = tasks,
                            isNightMode = isNightMode,
                            contentPaddingPx = paddingPx,
                            framePaddingPx = framePaddingPx,
                            outerStrokePx = outerStrokePx,
                        )?.let { onEmptyDoubleTap?.invoke(it) }
                    }
                },
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .then(clockGestureModifier)
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val framePadding = 28.dp.toPx()
            val outerRadius = size.minDimension / 2 - framePadding
            val outerStroke = 42.dp.toPx()
            val baseInnerStroke = outerStroke / 3.15f
            val exchangePulse = pulse + overshootLift
            val primaryStroke = outerStroke - (outerStroke - baseInnerStroke) * 0.22f * exchangePulse
            val secondaryStroke = baseInnerStroke + (outerStroke - baseInnerStroke) * 0.22f * exchangePulse

            val outerInnerRadius = outerRadius - primaryStroke
            val outerArcRadius = (outerRadius + outerInnerRadius) / 2
            val outerTopLeft = Offset(center.x - outerArcRadius, center.y - outerArcRadius)
            val outerSize = Size(outerArcRadius * 2, outerArcRadius * 2)

            val secondaryOuterRadius = outerInnerRadius
            val secondaryInnerRadius = secondaryOuterRadius - secondaryStroke
            val secondaryArcRadius = (secondaryOuterRadius + secondaryInnerRadius) / 2

            val tickOuterRadius = outerRadius + 6.dp.toPx()
            val tickInnerRadius = outerRadius - 5.dp.toPx()
            val labelRadius = outerRadius + 18.dp.toPx()
            val labelPaint = Paint().apply {
                isAntiAlias = true
                color = hourLabelColor.toArgb()
                textAlign = Paint.Align.CENTER
                textSize = 15.sp.toPx()
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val handAngle = ((currentTime.hour % 12) + currentTime.minute / 60f) * 30f - 90f
            val handRadians = Math.toRadians(handAngle.toDouble())
            val handLength = secondaryInnerRadius - 14.dp.toPx()
            val handEnd = Offset(
                x = center.x + cos(handRadians).toFloat() * handLength,
                y = center.y + sin(handRadians).toFloat() * handLength,
            )

            drawCircle(
                color = circleColor.copy(alpha = 0.42f + 0.14f * clampedProgress),
                radius = outerRadius,
                style = Stroke(width = 1.dp.toPx()),
            )
            drawCircle(
                color = circleColor.copy(alpha = 0.42f + 0.14f * clampedProgress),
                radius = outerInnerRadius,
                style = Stroke(width = 1.dp.toPx()),
            )

            repeat(12) { index ->
                val angle = Math.toRadians((index * 30f - 90f).toDouble())
                val tickStart = Offset(
                    x = center.x + cos(angle).toFloat() * tickInnerRadius,
                    y = center.y + sin(angle).toFloat() * tickInnerRadius,
                )
                val tickEnd = Offset(
                    x = center.x + cos(angle).toFloat() * tickOuterRadius,
                    y = center.y + sin(angle).toFloat() * tickOuterRadius,
                )
                drawLine(
                    color = hourTickColor,
                    start = tickStart,
                    end = tickEnd,
                    strokeWidth = 2.2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }

            val primaryInactiveColor = circleColor.copy(alpha = 0.05f + 0.02f * clampedProgress)
            drawArc(
                color = primaryInactiveColor,
                startAngle = 90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = outerTopLeft,
                size = outerSize,
                style = Stroke(width = primaryStroke, cap = StrokeCap.Butt),
            )

            val selectedWindow = if (isNightMode) ClockWindow.Night else ClockWindow.Day
            val selectedEmptySegments = selectedEmptySlot
                ?.takeIf { slot -> slot.endTotalMinutes > slot.startTotalMinutes }
                ?.let { slot -> segmentsForSlotInWindow(slot, selectedWindow) }
                .orEmpty()
            val emptySlotBounceFactor = if (selectedEmptySegments.isNotEmpty()) {
                selectedEmptySlotBounceProgress.coerceIn(0f, 1f)
            } else {
                0f
            }
            val emptyOuterRadius = outerArcRadius + emptySlotBounceFactor * 9.dp.toPx()
            val emptyInnerRadius = secondaryArcRadius + emptySlotBounceFactor * 6.2.dp.toPx()
            val emptyPrimaryStroke = primaryStroke * (1f + 0.2f * emptySlotBounceFactor)
            val emptySecondaryStroke = secondaryStroke * (1f + 0.24f * emptySlotBounceFactor)
            val emptyOuterTopLeft = Offset(
                center.x - emptyOuterRadius,
                center.y - emptyOuterRadius,
            )
            val emptyOuterSize = Size(
                emptyOuterRadius * 2,
                emptyOuterRadius * 2,
            )
            val emptyInnerTopLeft = Offset(
                center.x - emptyInnerRadius,
                center.y - emptyInnerRadius,
            )
            val emptyInnerSize = Size(
                emptyInnerRadius * 2,
                emptyInnerRadius * 2,
            )
            val emptyPrimaryCapAngleOffset = ((emptyPrimaryStroke * 0.18f) / (2f * Math.PI.toFloat() * emptyOuterRadius)) * 360f
            val emptySecondaryCapAngleOffset = ((emptySecondaryStroke * 0.18f) / (2f * Math.PI.toFloat() * emptyInnerRadius)) * 360f
            val emptySelectedColor = if (isNightMode) {
                lerp(controlFillColor, Color.White, 0.28f)
            } else {
                lerp(controlFillColor, colorScheme.onSurface, 0.14f)
            }
            val emptyOuterAlpha = (0.82f + 0.14f * pulse + emptySlotBounceFactor * 0.18f).coerceAtLeast(0f)
            val emptyInnerAlpha = (0.66f + 0.1f * pulse + emptySlotBounceFactor * 0.13f).coerceAtLeast(0f)

            val dayOuterWeight = (1f - clampedProgress + overshootLift * 0.65f).coerceIn(0f, 1.15f)
            val dayInnerWeight = (clampedProgress - overshootLift * 0.35f).coerceIn(0f, 1.15f)
            val nightOuterWeight = (clampedProgress - overshootLift * 0.35f).coerceIn(0f, 1.15f)
            val nightInnerWeight = (1f - clampedProgress + overshootLift * 0.65f).coerceIn(0f, 1.15f)

            if (selectedEmptySegments.isNotEmpty()) {
                drawWindowSegments(
                    segments = selectedEmptySegments,
                    color = emptySelectedColor,
                    topLeft = emptyOuterTopLeft,
                    size = emptyOuterSize,
                    strokeWidth = emptyPrimaryStroke,
                    capAngleOffset = emptyPrimaryCapAngleOffset,
                    alpha = emptyOuterAlpha,
                    startAngleShift = if (selectedWindow == ClockWindow.Day) -sweepDrift else sweepDrift,
                    pinOuterEdge = false,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = selectedEmptySegments,
                    color = emptySelectedColor,
                    topLeft = emptyInnerTopLeft,
                    size = emptyInnerSize,
                    strokeWidth = emptySecondaryStroke,
                    capAngleOffset = emptySecondaryCapAngleOffset,
                    alpha = emptyInnerAlpha,
                    startAngleShift = if (selectedWindow == ClockWindow.Day) sweepDrift * 0.55f else -sweepDrift * 0.55f,
                    pinOuterEdge = true,
                    renderQuality = renderQuality,
                )
            }

            cachedTaskSegments.forEach { (task, segmentsByWindow) ->
                val isPinnedPeakTask = task.id == pinnedPeakTaskId
                val isPressedPeakTask = task.id == visualPressedTaskId.value
                val isSelectedTask = task.id == selectedTaskId
                val selectedTaskPulseFactor = if (isSelectedTask) {
                    selectedTaskBounceProgress.coerceIn(0f, 1f)
                } else {
                    0f
                }
                val taskBounceFactor = when {
                    isPinnedPeakTask -> 0.74f
                    isPressedPeakTask -> taskPressHold.value.coerceIn(0f, 0.74f)
                    else -> 0f
                }
                val outerBounceRadius = outerArcRadius + taskBounceFactor * 9.dp.toPx()
                val innerBounceRadius = secondaryArcRadius + taskBounceFactor * 6.2.dp.toPx()
                val taskPrimaryStroke = primaryStroke * (1f + 0.2f * taskBounceFactor)
                val taskSecondaryStroke = secondaryStroke * (1f + 0.24f * taskBounceFactor)
                val taskOuterTopLeft = Offset(
                    center.x - outerBounceRadius,
                    center.y - outerBounceRadius,
                )
                val taskOuterSize = Size(
                    outerBounceRadius * 2,
                    outerBounceRadius * 2,
                )
                val taskSecondaryTopLeft = Offset(
                    center.x - innerBounceRadius,
                    center.y - innerBounceRadius,
                )
                val taskSecondarySize = Size(
                    innerBounceRadius * 2,
                    innerBounceRadius * 2,
                )
                val taskPrimaryCapAngleOffset = ((taskPrimaryStroke * 0.18f) / (2f * Math.PI.toFloat() * outerBounceRadius)) * 360f
                val taskSecondaryCapAngleOffset = ((taskSecondaryStroke * 0.18f) / (2f * Math.PI.toFloat() * innerBounceRadius)) * 360f
                val taskColor = when {
                    isPinnedPeakTask || isPressedPeakTask -> lerp(task.color, Color.White, if (isNightMode) 0.24f else 0.17f)
                    selectedTaskPulseFactor > 0.01f -> lerp(task.color, Color.White, (if (isNightMode) 0.08f else 0.05f) + selectedTaskPulseFactor * if (isNightMode) 0.06f else 0.04f)
                    else -> task.color
                }
                val dayOuterAlpha = (0.72f * dayOuterWeight + 0.12f * pulse + taskBounceFactor * 0.14f).coerceAtLeast(0f)
                val dayInnerAlpha = (0.56f * dayInnerWeight + 0.08f * pulse + taskBounceFactor * 0.1f).coerceAtLeast(0f)
                val nightOuterAlpha = (0.78f * nightOuterWeight + 0.12f * pulse + taskBounceFactor * 0.16f).coerceAtLeast(0f)
                val nightInnerAlpha = (0.62f * nightInnerWeight + 0.08f * pulse + taskBounceFactor * 0.11f).coerceAtLeast(0f)

                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Day),
                    color = taskColor,
                    topLeft = taskOuterTopLeft,
                    size = taskOuterSize,
                    strokeWidth = taskPrimaryStroke,
                    capAngleOffset = taskPrimaryCapAngleOffset,
                    alpha = dayOuterAlpha,
                    startAngleShift = -sweepDrift,
                    pinOuterEdge = false,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Day),
                    color = taskColor,
                    topLeft = taskSecondaryTopLeft,
                    size = taskSecondarySize,
                    strokeWidth = taskSecondaryStroke,
                    capAngleOffset = taskSecondaryCapAngleOffset,
                    alpha = dayInnerAlpha,
                    startAngleShift = sweepDrift * 0.55f,
                    pinOuterEdge = true,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Night),
                    color = taskColor,
                    topLeft = taskOuterTopLeft,
                    size = taskOuterSize,
                    strokeWidth = taskPrimaryStroke,
                    capAngleOffset = taskPrimaryCapAngleOffset,
                    alpha = nightOuterAlpha,
                    startAngleShift = sweepDrift,
                    pinOuterEdge = false,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Night),
                    color = taskColor,
                    topLeft = taskSecondaryTopLeft,
                    size = taskSecondarySize,
                    strokeWidth = taskSecondaryStroke,
                    capAngleOffset = taskSecondaryCapAngleOffset,
                    alpha = nightInnerAlpha,
                    startAngleShift = -sweepDrift * 0.55f,
                    pinOuterEdge = true,
                    renderQuality = renderQuality,
                )
            }


            drawLine(
                color = handColor,
                start = center,
                end = handEnd,
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawCircle(
                color = handColor,
                radius = 5.dp.toPx(),
                center = center,
            )

            (1..12).forEach { hour ->
                val angle = Math.toRadians((hour * 30f - 90f).toDouble())
                val x = center.x + cos(angle).toFloat() * labelRadius
                val y = center.y + sin(angle).toFloat() * labelRadius
                val baselineShift = (labelPaint.descent() + labelPaint.ascent()) / 2f
                drawContext.canvas.nativeCanvas.drawText(
                    hour.toString(),
                    x,
                    y - baselineShift,
                    labelPaint,
                )
            }
        }
    }
}

private enum class ClockWindow {
    Day,
    Night,
}

private data class ClockSegment(
    val startFraction: Float,
    val sweepFraction: Float,
    val touchesWindowStart: Boolean,
    val touchesWindowEnd: Boolean,
)

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawWindowSegments(
    segments: List<ClockSegment>,
    color: Color,
    topLeft: Offset,
    size: Size,
    strokeWidth: Float,
    capAngleOffset: Float,
    alpha: Float,
    startAngleShift: Float,
    pinOuterEdge: Boolean,
    renderQuality: ClockFaceRenderQuality,
) {
    if (alpha <= 0.01f || segments.isEmpty()) return

    val baseColor = color.copy(alpha = alpha.coerceIn(0f, 1f))
    val baseRadius = size.width / 2f
    val center = Offset(topLeft.x + baseRadius, topLeft.y + baseRadius)
    val taperAngle = if (renderQuality == ClockFaceRenderQuality.Full) 18f else 10f
    val minStrokeWidth = strokeWidth * if (renderQuality == ClockFaceRenderQuality.Full) 0.58f else 0.72f

    segments.forEach { segment ->
        val rawSweepAngle = segment.sweepFraction * 360f
        val minSweep = if (strokeWidth > 20f) 6f else 4f
        val adjustedSweepAngle = (rawSweepAngle - capAngleOffset).coerceAtLeast(minSweep)
        val adjustedStartAngle = 90f + startAngleShift + segment.startFraction * 360f + (rawSweepAngle - adjustedSweepAngle) / 2f

        val startTaperAngle = if (segment.touchesWindowStart) min(taperAngle, adjustedSweepAngle * 0.36f) else 0f
        val endTaperAngle = if (segment.touchesWindowEnd) min(taperAngle, adjustedSweepAngle * 0.36f) else 0f
        val middleSweep = adjustedSweepAngle - startTaperAngle - endTaperAngle

        if (renderQuality == ClockFaceRenderQuality.Full && startTaperAngle > 0.2f) {
            drawPinnedTaperArc(
                color = baseColor,
                center = center,
                baseRadius = baseRadius,
                baseStrokeWidth = strokeWidth,
                minStrokeWidth = minStrokeWidth,
                startAngle = adjustedStartAngle,
                sweepAngle = startTaperAngle,
                pinOuterEdge = pinOuterEdge,
                growsFromBoundary = true,
            )
        }

        if (middleSweep > 0.2f) {
            drawArc(
                color = baseColor,
                startAngle = adjustedStartAngle + if (renderQuality == ClockFaceRenderQuality.Full) startTaperAngle else 0f,
                sweepAngle = middleSweep.coerceAtLeast(0.2f),
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            )
        }

        if (renderQuality == ClockFaceRenderQuality.Full && endTaperAngle > 0.2f) {
            drawPinnedTaperArc(
                color = baseColor,
                center = center,
                baseRadius = baseRadius,
                baseStrokeWidth = strokeWidth,
                minStrokeWidth = minStrokeWidth,
                startAngle = adjustedStartAngle + adjustedSweepAngle - endTaperAngle,
                sweepAngle = endTaperAngle,
                pinOuterEdge = pinOuterEdge,
                growsFromBoundary = false,
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPinnedTaperArc(
    color: Color,
    center: Offset,
    baseRadius: Float,
    baseStrokeWidth: Float,
    minStrokeWidth: Float,
    startAngle: Float,
    sweepAngle: Float,
    pinOuterEdge: Boolean,
    growsFromBoundary: Boolean,
) {
    val sampleCount = max(14, (sweepAngle / 1.1f).toInt())
    val outerEdgeRadius = baseRadius + baseStrokeWidth / 2f
    val innerEdgeRadius = baseRadius - baseStrokeWidth / 2f
    val outerBoundary = ArrayList<Offset>(sampleCount + 1)
    val innerBoundary = ArrayList<Offset>(sampleCount + 1)

    repeat(sampleCount + 1) { index ->
        val progress = index / sampleCount.toFloat()
        val boundaryProgress = if (growsFromBoundary) progress else 1f - progress
        val easedProgress = boundaryProgress * boundaryProgress * boundaryProgress
        val currentStrokeWidth = lerpFloat(minStrokeWidth, baseStrokeWidth, easedProgress)
        val currentOuterRadius = if (pinOuterEdge) {
            outerEdgeRadius
        } else {
            innerEdgeRadius + currentStrokeWidth
        }
        val currentInnerRadius = if (pinOuterEdge) {
            outerEdgeRadius - currentStrokeWidth
        } else {
            innerEdgeRadius
        }
        val angle = startAngle + sweepAngle * progress
        outerBoundary += pointOnCircle(center = center, radius = currentOuterRadius, angleDegrees = angle)
        innerBoundary += pointOnCircle(center = center, radius = currentInnerRadius, angleDegrees = angle)
    }

    val taperPath = Path().apply {
        moveTo(outerBoundary.first().x, outerBoundary.first().y)
        outerBoundary.drop(1).forEach { point ->
            lineTo(point.x, point.y)
        }
        innerBoundary.asReversed().forEach { point ->
            lineTo(point.x, point.y)
        }
        close()
    }

    drawPath(
        path = taperPath,
        color = color,
    )
}



private fun emptySlotsForWindow(tasks: List<TimeTask>, window: ClockWindow): List<ClockEmptySlot> {
    val windowRange = when (window) {
        ClockWindow.Day -> 6 * 60 to 18 * 60
        ClockWindow.Night -> 18 * 60 to 30 * 60
    }
    val occupiedRanges = tasks.flatMap { task ->
        val startAbsolute = task.startHour * 60 + task.startMinute
        val normalizedEnd = if (task.endHour * 60 + task.endMinute <= startAbsolute) {
            task.endHour * 60 + task.endMinute + 24 * 60
        } else {
            task.endHour * 60 + task.endMinute
        }
        listOf(
            startAbsolute to normalizedEnd,
            startAbsolute + 24 * 60 to normalizedEnd + 24 * 60,
        )
    }.mapNotNull { range ->
        val overlapStart = max(windowRange.first, range.first)
        val overlapEnd = min(windowRange.second, range.second)
        if (overlapEnd > overlapStart) overlapStart to overlapEnd else null
    }.sortedBy { it.first }

    if (occupiedRanges.isEmpty()) {
        return listOf(
            ClockEmptySlot(
                startTotalMinutes = windowRange.first,
                endTotalMinutes = windowRange.second,
            ),
        )
    }

    val mergedRanges = mutableListOf<Pair<Int, Int>>()
    occupiedRanges.forEach { range ->
        if (mergedRanges.isEmpty()) {
            mergedRanges += range
        } else {
            val lastRange = mergedRanges.last()
            if (range.first <= lastRange.second) {
                mergedRanges[mergedRanges.lastIndex] = lastRange.first to max(lastRange.second, range.second)
            } else {
                mergedRanges += range
            }
        }
    }

    var cursor = windowRange.first
    return buildList {
        mergedRanges.forEach { range ->
            if (range.first > cursor) {
                add(
                    ClockEmptySlot(
                        startTotalMinutes = cursor,
                        endTotalMinutes = range.first,
                    ),
                )
            }
            cursor = max(cursor, range.second)
        }
        if (cursor < windowRange.second) {
            add(
                ClockEmptySlot(
                    startTotalMinutes = cursor,
                    endTotalMinutes = windowRange.second,
                ),
            )
        }
    }
}

private fun segmentsForSlotInWindow(slot: ClockEmptySlot, window: ClockWindow): List<ClockSegment> {
    val windowRange = when (window) {
        ClockWindow.Day -> 6 * 60 to 18 * 60
        ClockWindow.Night -> 18 * 60 to 30 * 60
    }
    val overlapStart = max(windowRange.first, slot.startTotalMinutes)
    val overlapEnd = min(windowRange.second, slot.endTotalMinutes)
    if (overlapEnd <= overlapStart) return emptyList()

    return listOf(
        ClockSegment(
            startFraction = (overlapStart - windowRange.first) / (12f * 60f),
            sweepFraction = (overlapEnd - overlapStart) / (12f * 60f),
            touchesWindowStart = overlapStart == windowRange.first && slot.startTotalMinutes < windowRange.first,
            touchesWindowEnd = overlapEnd == windowRange.second && slot.endTotalMinutes > windowRange.second,
        ),
    )
}

private fun hitTestClockEmptySlot(
    tapOffset: Offset,
    canvasSize: IntSize,
    tasks: List<TimeTask>,
    isNightMode: Boolean,
    contentPaddingPx: Float,
    framePaddingPx: Float,
    outerStrokePx: Float,
): ClockEmptySlot? {
    val width = canvasSize.width.toFloat()
    val height = canvasSize.height.toFloat()
    if (width <= 0f || height <= 0f) return null

    val drawableWidth = width - contentPaddingPx * 2
    val drawableHeight = height - contentPaddingPx * 2
    if (drawableWidth <= 0f || drawableHeight <= 0f) return null

    val localOffset = Offset(
        x = tapOffset.x - contentPaddingPx,
        y = tapOffset.y - contentPaddingPx,
    )
    val center = Offset(drawableWidth / 2f, drawableHeight / 2f)
    val outerRadius = min(drawableWidth, drawableHeight) / 2f - framePaddingPx
    if (outerRadius <= 0f) return null

    val baseInnerStroke = outerStrokePx / 3.15f
    val primaryStroke = outerStrokePx
    val activeStrokeCenterRadius = outerRadius - primaryStroke / 2f
    val activeBandHalfThickness = max(primaryStroke / 2f, baseInnerStroke)
    val distance = hypot(localOffset.x - center.x, localOffset.y - center.y)
    if (distance !in (activeStrokeCenterRadius - activeBandHalfThickness)..(activeStrokeCenterRadius + activeBandHalfThickness)) {
        return null
    }

    val angle = (Math.toDegrees(atan2((localOffset.y - center.y).toDouble(), (localOffset.x - center.x).toDouble())) + 360.0) % 360.0
    val normalizedFraction = (((angle + 270.0) % 360.0) / 360.0).toFloat()
    val targetWindow = if (isNightMode) ClockWindow.Night else ClockWindow.Day
    val targetWindowStart = if (isNightMode) 18 * 60 else 6 * 60
    val tappedMinutes = targetWindowStart + (normalizedFraction * 12f * 60f).roundToInt().coerceIn(0, 12 * 60)
    val snappedMinutes = snapToNearestHalfHour(tappedMinutes)

    return emptySlotsForWindow(tasks, targetWindow)
        .firstOrNull { slot -> snappedMinutes in slot.startTotalMinutes..slot.endTotalMinutes }
        ?.let { slot ->
            val anchor = snappedMinutes.coerceIn(slot.startTotalMinutes, slot.endTotalMinutes)
            val preferredStart = (anchor - 30).coerceAtLeast(slot.startTotalMinutes)
            val preferredEnd = (anchor + 30).coerceAtMost(slot.endTotalMinutes)
            val minDuration = min(30, slot.endTotalMinutes - slot.startTotalMinutes)
            val adjustedStart = when {
                preferredEnd - preferredStart >= minDuration -> preferredStart
                preferredEnd - minDuration >= slot.startTotalMinutes -> preferredEnd - minDuration
                else -> slot.startTotalMinutes
            }
            val adjustedEnd = max(adjustedStart + minDuration, preferredEnd).coerceAtMost(slot.endTotalMinutes)
            ClockEmptySlot(
                startTotalMinutes = adjustedStart,
                endTotalMinutes = adjustedEnd,
            )
        }
}

private fun snapToNearestHalfHour(totalMinutes: Int): Int {
    val lower = (totalMinutes / 30) * 30
    val upper = lower + 30
    return if (totalMinutes - lower < upper - totalMinutes) lower else upper
}

private fun segmentsForWindow(task: TimeTask, window: ClockWindow): List<ClockSegment> {
    val startAbsolute = task.startHour * 60 + task.startMinute
    val endAbsolute = task.endHour * 60 + task.endMinute
    val normalizedEnd = if (endAbsolute <= startAbsolute) endAbsolute + 24 * 60 else endAbsolute
    val windowRange = when (window) {
        ClockWindow.Day -> 6 * 60 to 18 * 60
        ClockWindow.Night -> 18 * 60 to 30 * 60
    }

    val taskRanges = listOf(
        startAbsolute to normalizedEnd,
        startAbsolute + 24 * 60 to normalizedEnd + 24 * 60,
    )

    return buildList {
        taskRanges.forEach { range ->
            val overlapStart = max(windowRange.first, range.first)
            val overlapEnd = min(windowRange.second, range.second)
            if (overlapEnd > overlapStart) {
                add(
                    ClockSegment(
                        startFraction = (overlapStart - windowRange.first) / (12f * 60f),
                        sweepFraction = (overlapEnd - overlapStart) / (12f * 60f),
                        touchesWindowStart = overlapStart == windowRange.first && range.first < windowRange.first,
                        touchesWindowEnd = overlapEnd == windowRange.second && range.second > windowRange.second,
                    ),
                )
            }
        }
    }
}




private fun hitTestClockTask(
    tapOffset: Offset,
    canvasSize: IntSize,
    tasks: List<TimeTask>,
    isNightMode: Boolean,
    contentPaddingPx: Float,
    framePaddingPx: Float,
    outerStrokePx: Float,
): TimeTask? {
    if (tasks.isEmpty()) return null

    val width = canvasSize.width.toFloat()
    val height = canvasSize.height.toFloat()
    if (width <= 0f || height <= 0f) return null

    val drawableWidth = width - contentPaddingPx * 2
    val drawableHeight = height - contentPaddingPx * 2
    if (drawableWidth <= 0f || drawableHeight <= 0f) return null

    val localOffset = Offset(
        x = tapOffset.x - contentPaddingPx,
        y = tapOffset.y - contentPaddingPx,
    )
    val center = Offset(drawableWidth / 2f, drawableHeight / 2f)
    val outerRadius = min(drawableWidth, drawableHeight) / 2f - framePaddingPx
    if (outerRadius <= 0f) return null

    val baseInnerStroke = outerStrokePx / 3.15f
    val primaryStroke = outerStrokePx
    val targetWindow = if (isNightMode) ClockWindow.Night else ClockWindow.Day
    val activeStrokeCenterRadius = outerRadius - primaryStroke / 2f
    val activeBandHalfThickness = max(primaryStroke / 2f, baseInnerStroke)
    val distance = hypot(localOffset.x - center.x, localOffset.y - center.y)
    if (distance !in (activeStrokeCenterRadius - activeBandHalfThickness)..(activeStrokeCenterRadius + activeBandHalfThickness)) {
        return null
    }

    val angle = (Math.toDegrees(atan2((localOffset.y - center.y).toDouble(), (localOffset.x - center.x).toDouble())) + 360.0) % 360.0
    val normalizedFraction = (((angle + 270.0) % 360.0) / 360.0).toFloat()

    return tasks
        .asReversed()
        .firstOrNull { task ->
            segmentsForWindow(task, targetWindow).any { segment ->
                val segmentStart = segment.startFraction
                val segmentEnd = segment.startFraction + segment.sweepFraction
                normalizedFraction in segmentStart..segmentEnd
            }
        }
}

private fun lerpFloat(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction.coerceIn(0f, 1f)
}

private fun pointOnCircle(center: Offset, radius: Float, angleDegrees: Float): Offset {
    val radians = Math.toRadians(angleDegrees.toDouble())
    return Offset(
        x = center.x + cos(radians).toFloat() * radius,
        y = center.y + sin(radians).toFloat() * radius,
    )
}
