package com.vtm.app.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtm.app.model.TimeTask
import com.vtm.app.ui.theme.ClockCircleDark
import com.vtm.app.ui.theme.ClockCircleLight
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
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
) {
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

    Box(
        modifier = modifier
            .aspectRatio(1f)
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
            val outerCapAngleOffset = ((primaryStroke * 0.18f) / (2f * Math.PI.toFloat() * outerArcRadius)) * 360f

            val secondaryOuterRadius = outerInnerRadius
            val secondaryInnerRadius = secondaryOuterRadius - secondaryStroke
            val secondaryArcRadius = (secondaryOuterRadius + secondaryInnerRadius) / 2
            val secondaryTopLeft = Offset(center.x - secondaryArcRadius, center.y - secondaryArcRadius)
            val secondarySize = Size(secondaryArcRadius * 2, secondaryArcRadius * 2)
            val secondaryCapAngleOffset = ((secondaryStroke * 0.18f) / (2f * Math.PI.toFloat() * secondaryArcRadius)) * 360f

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

            val dayOuterWeight = (1f - clampedProgress + overshootLift * 0.65f).coerceIn(0f, 1.15f)
            val dayInnerWeight = (clampedProgress - overshootLift * 0.35f).coerceIn(0f, 1.15f)
            val nightOuterWeight = (clampedProgress - overshootLift * 0.35f).coerceIn(0f, 1.15f)
            val nightInnerWeight = (1f - clampedProgress + overshootLift * 0.65f).coerceIn(0f, 1.15f)

            cachedTaskSegments.forEach { (task, segmentsByWindow) ->
                val dayOuterAlpha = (0.72f * dayOuterWeight + 0.12f * pulse).coerceAtLeast(0f)
                val dayInnerAlpha = (0.56f * dayInnerWeight + 0.08f * pulse).coerceAtLeast(0f)
                val nightOuterAlpha = (0.78f * nightOuterWeight + 0.12f * pulse).coerceAtLeast(0f)
                val nightInnerAlpha = (0.62f * nightInnerWeight + 0.08f * pulse).coerceAtLeast(0f)

                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Day),
                    color = task.color,
                    topLeft = outerTopLeft,
                    size = outerSize,
                    strokeWidth = primaryStroke,
                    capAngleOffset = outerCapAngleOffset,
                    alpha = dayOuterAlpha,
                    startAngleShift = -sweepDrift,
                    pinOuterEdge = false,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Day),
                    color = task.color,
                    topLeft = secondaryTopLeft,
                    size = secondarySize,
                    strokeWidth = secondaryStroke,
                    capAngleOffset = secondaryCapAngleOffset,
                    alpha = dayInnerAlpha,
                    startAngleShift = sweepDrift * 0.55f,
                    pinOuterEdge = true,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Night),
                    color = task.color,
                    topLeft = outerTopLeft,
                    size = outerSize,
                    strokeWidth = primaryStroke,
                    capAngleOffset = outerCapAngleOffset,
                    alpha = nightOuterAlpha,
                    startAngleShift = sweepDrift,
                    pinOuterEdge = false,
                    renderQuality = renderQuality,
                )
                drawWindowSegments(
                    segments = segmentsByWindow.getValue(ClockWindow.Night),
                    color = task.color,
                    topLeft = secondaryTopLeft,
                    size = secondarySize,
                    strokeWidth = secondaryStroke,
                    capAngleOffset = secondaryCapAngleOffset,
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
