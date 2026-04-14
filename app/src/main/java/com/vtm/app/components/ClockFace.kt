package com.vtm.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtm.app.model.TimeTask
import com.vtm.app.ui.theme.ClockCircleDark
import com.vtm.app.ui.theme.ClockCircleLight
import kotlin.math.max
import kotlin.math.min

@Composable
fun ClockFace(
    isNightMode: Boolean,
    tasks: List<TimeTask>,
    centerTitle: String,
    centerSubtitle: String,
    modifier: Modifier = Modifier,
) {
    val circleColor = if (isNightMode) ClockCircleDark else ClockCircleLight
    val centerTextColor = MaterialTheme.colorScheme.onBackground
    val activeWindowLabel = if (isNightMode) "18:00-6:00" else "6:00-18:00"

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            val outerRadius = size.minDimension / 2 - strokeWidth
            val innerRadius = outerRadius - 64.dp.toPx()
            val arcRadius = (outerRadius + innerRadius) / 2
            val arcStroke = outerRadius - innerRadius
            val ringTopLeft = Offset(center.x - arcRadius, center.y - arcRadius)
            val ringSize = Size(arcRadius * 2, arcRadius * 2)

            drawCircle(
                color = circleColor,
                radius = outerRadius,
                style = Stroke(width = 1.dp.toPx()),
            )
            drawCircle(
                color = circleColor,
                radius = innerRadius,
                style = Stroke(width = 1.dp.toPx()),
            )

            val inactiveColor = circleColor.copy(alpha = 0.16f)
            drawArc(
                color = inactiveColor,
                startAngle = 90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = ringTopLeft,
                size = ringSize,
                style = Stroke(width = arcStroke, cap = StrokeCap.Round),
            )

            tasks.forEach { task ->
                segmentsForWindow(task, isNightMode).forEach { segment ->
                    drawArc(
                        color = task.color,
                        startAngle = 90f + segment.startFraction * 360f,
                        sweepAngle = segment.sweepFraction * 360f,
                        useCenter = false,
                        topLeft = ringTopLeft,
                        size = ringSize,
                        style = Stroke(width = arcStroke, cap = StrokeCap.Round),
                    )
                }
            }
        }

        Text(
            text = "12",
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 30.dp),
            fontSize = 13.sp,
            color = circleColor.copy(alpha = 0.72f),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "3",
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 30.dp),
            fontSize = 13.sp,
            color = circleColor.copy(alpha = 0.72f),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "6",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp),
            fontSize = 13.sp,
            color = circleColor.copy(alpha = 0.72f),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "9",
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 30.dp),
            fontSize = 13.sp,
            color = circleColor.copy(alpha = 0.72f),
            fontWeight = FontWeight.Medium,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 42.dp),
        ) {
            Text(
                text = activeWindowLabel,
                color = centerTextColor.copy(alpha = 0.38f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private data class ClockSegment(
    val startFraction: Float,
    val sweepFraction: Float,
)

private fun segmentsForWindow(task: TimeTask, isNightMode: Boolean): List<ClockSegment> {
    val startAbsolute = task.startHour * 60 + task.startMinute
    val endAbsolute = task.endHour * 60 + task.endMinute
    val normalizedEnd = if (endAbsolute <= startAbsolute) endAbsolute + 24 * 60 else endAbsolute
    val windows = if (isNightMode) {
        listOf(18 * 60 to 30 * 60)
    } else {
        listOf(6 * 60 to 18 * 60)
    }

    val taskRanges = listOf(
        startAbsolute to normalizedEnd,
        startAbsolute + 24 * 60 to normalizedEnd + 24 * 60,
    )

    return buildList {
        windows.forEach { window ->
            taskRanges.forEach { range ->
                val overlapStart = max(window.first, range.first)
                val overlapEnd = min(window.second, range.second)
                if (overlapEnd > overlapStart) {
                    add(
                        ClockSegment(
                            startFraction = (overlapStart - window.first) / (12f * 60f),
                            sweepFraction = (overlapEnd - overlapStart) / (12f * 60f),
                        ),
                    )
                }
            }
        }
    }
}
