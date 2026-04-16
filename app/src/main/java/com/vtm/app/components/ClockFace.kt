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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
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

@Composable
fun ClockFace(
    isNightMode: Boolean,
    tasks: List<TimeTask>,
    centerTitle: String,
    centerSubtitle: String,
    modifier: Modifier = Modifier,
) {
    val transitionProgress by animateFloatAsState(
        targetValue = if (isNightMode) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = Spring.StiffnessLow,
        ),
        label = "clock-mode-progress",
    )
    val clampedProgress = transitionProgress.coerceIn(0f, 1f)
    val overshootLift = (transitionProgress - clampedProgress) * 0.9f
    val pulse = sin((clampedProgress * Math.PI).toFloat())
    val sweepDrift = (overshootLift + pulse * 0.12f) * 8f

    val circleColor = lerp(ClockCircleLight, ClockCircleDark, clampedProgress)
    val centerTextColor = MaterialTheme.colorScheme.onBackground
    val hourLabelColor = centerTextColor.copy(alpha = 0.84f + 0.08f * clampedProgress)
    val hourTickColor = centerTextColor.copy(alpha = 0.22f + 0.06f * clampedProgress)
    val handColor = centerTextColor.copy(alpha = 0.9f + 0.08f * clampedProgress)
    val currentTime = LocalTime.now()

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

            tasks.forEach { task ->
                val dayOuterAlpha = (0.72f * dayOuterWeight + 0.12f * pulse).coerceAtLeast(0f)
                val dayInnerAlpha = (0.56f * dayInnerWeight + 0.08f * pulse).coerceAtLeast(0f)
                val nightOuterAlpha = (0.78f * nightOuterWeight + 0.12f * pulse).coerceAtLeast(0f)
                val nightInnerAlpha = (0.62f * nightInnerWeight + 0.08f * pulse).coerceAtLeast(0f)

                drawWindowSegments(
                    task = task,
                    window = ClockWindow.Day,
                    topLeft = outerTopLeft,
                    size = outerSize,
                    strokeWidth = primaryStroke,
                    capAngleOffset = outerCapAngleOffset,
                    alpha = dayOuterAlpha,
                    startAngleShift = -sweepDrift,
                    pinOuterEdge = false,
                )
                drawWindowSegments(
                    task = task,
                    window = ClockWindow.Day,
                    topLeft = secondaryTopLeft,
                    size = secondarySize,
                    strokeWidth = secondaryStroke,
                    capAngleOffset = secondaryCapAngleOffset,
                    alpha = dayInnerAlpha,
                    startAngleShift = sweepDrift * 0.55f,
                    pinOuterEdge = true,
                )
                drawWindowSegments(
                    task = task,
                    window = ClockWindow.Night,
                    topLeft = outerTopLeft,
                    size = outerSize,
                    strokeWidth = primaryStroke,
                    capAngleOffset = outerCapAngleOffset,
                    alpha = nightOuterAlpha,
                    startAngleShift = sweepDrift,
                    pinOuterEdge = false,
                )
                drawWindowSegments(
                    task = task,
                    window = ClockWindow.Night,
                    topLeft = secondaryTopLeft,
                    size = secondarySize,
                    strokeWidth = secondaryStroke,
                    capAngleOffset = secondaryCapAngleOffset,
                    alpha = nightInnerAlpha,
                    startAngleShift = -sweepDrift * 0.55f,
                    pinOuterEdge = true,
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
    task: TimeTask,
    window: ClockWindow,
    topLeft: Offset,
    size: Size,
    strokeWidth: Float,
    capAngleOffset: Float,
    alpha: Float,
    startAngleShift: Float,
    pinOuterEdge: Boolean,
) {
    if (alpha <= 0.01f) return

    val baseColor = task.color.copy(alpha = alpha.coerceIn(0f, 1f))
    val baseRadius = size.width / 2f
    val center = Offset(topLeft.x + baseRadius, topLeft.y + baseRadius)
    val taperAngle = 18f
    val minStrokeWidth = strokeWidth * 0.58f

    segmentsForWindow(task, window).forEach { segment ->
        val rawSweepAngle = segment.sweepFraction * 360f
        val minSweep = if (strokeWidth > 20f) 6f else 4f
        val adjustedSweepAngle = (rawSweepAngle - capAngleOffset).coerceAtLeast(minSweep)
        val adjustedStartAngle = 90f + startAngleShift + segment.startFraction * 360f + (rawSweepAngle - adjustedSweepAngle) / 2f

        val startTaperAngle = if (segment.touchesWindowStart) min(taperAngle, adjustedSweepAngle * 0.36f) else 0f
        val endTaperAngle = if (segment.touchesWindowEnd) min(taperAngle, adjustedSweepAngle * 0.36f) else 0f
        val middleSweep = adjustedSweepAngle - startTaperAngle - endTaperAngle

        if (startTaperAngle > 0.2f) {
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
                startAngle = adjustedStartAngle + startTaperAngle,
                sweepAngle = middleSweep,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            )
        }

        if (endTaperAngle > 0.2f) {
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
    val sliceCount = max(64, (sweepAngle / 0.24f).toInt())
    val sliceSweep = sweepAngle / sliceCount
    val outerEdgeRadius = baseRadius + baseStrokeWidth / 2f
    val innerEdgeRadius = baseRadius - baseStrokeWidth / 2f

    repeat(sliceCount) { index ->
        val progress = (index + 0.5f) / sliceCount.toFloat()
        val boundaryProgress = if (growsFromBoundary) progress else 1f - progress
        val easedProgress = boundaryProgress * boundaryProgress * boundaryProgress
        val currentStrokeWidth = lerpFloat(minStrokeWidth, baseStrokeWidth, easedProgress)
        val currentRadius = if (pinOuterEdge) {
            outerEdgeRadius - currentStrokeWidth / 2f
        } else {
            innerEdgeRadius + currentStrokeWidth / 2f
        }
        val currentTopLeft = Offset(center.x - currentRadius, center.y - currentRadius)
        val currentSize = Size(currentRadius * 2, currentRadius * 2)

        drawArc(
            color = color,
            startAngle = startAngle + sliceSweep * index,
            sweepAngle = sliceSweep,
            useCenter = false,
            topLeft = currentTopLeft,
            size = currentSize,
            style = Stroke(width = currentStrokeWidth, cap = StrokeCap.Butt),
        )
    }
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
