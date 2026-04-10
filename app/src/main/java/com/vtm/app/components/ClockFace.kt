package com.vtm.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtm.app.model.TimeTask
import com.vtm.app.ui.theme.ClockCircleDark
import com.vtm.app.ui.theme.ClockCircleLight

@Composable
fun ClockFace(
    isNightMode: Boolean,
    tasks: List<TimeTask>,
    modifier: Modifier = Modifier
) {
    val isSystemDark = isSystemInDarkTheme()
    val circleColor = if (isSystemDark) ClockCircleDark else ClockCircleLight
    val centerTextColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 12.dp.toPx()
            val radius = size.minDimension / 2 - strokeWidth
            val innerRadius = radius - 48.dp.toPx()

            // Draw outer and inner circles
            drawCircle(
                color = circleColor,
                radius = radius,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = circleColor,
                radius = innerRadius,
                style = Stroke(width = 1.dp.toPx())
            )

            // Draw tasks as arcs
            val arcRadius = (radius + innerRadius) / 2
            val arcStroke = radius - innerRadius

            tasks.forEach { task ->
                val offsetHours = if (isNightMode) 12 else 0
                val startHourFloat = task.startHour + task.startMinute / 60f
                val endHourFloat = task.endHour + task.endMinute / 60f

                // Draw if falls in current 12-hour window
                if (startHourFloat < offsetHours + 12 && endHourFloat > offsetHours) {
                    val drawStart = maxOf(startHourFloat, offsetHours.toFloat()) - offsetHours
                    val drawEnd = minOf(endHourFloat, (offsetHours + 12).toFloat()) - offsetHours

                    val startAngle = -90f + (drawStart / 12f) * 360f
                    val sweepAngle = ((drawEnd - drawStart) / 12f) * 360f

                    drawArc(
                        color = task.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(
                            center.x - arcRadius,
                            center.y - arcRadius
                        ),
                        size = Size(arcRadius * 2, arcRadius * 2),
                        style = Stroke(width = arcStroke, cap = StrokeCap.Butt)
                    )
                }
            }
        }

        // Labels
        Text("12", modifier = Modifier.align(Alignment.TopCenter).padding(top = 40.dp), fontSize = 14.sp, color = circleColor)
        Text("3", modifier = Modifier.align(Alignment.CenterEnd).padding(end = 40.dp), fontSize = 14.sp, color = circleColor)
        Text("6", modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp), fontSize = 14.sp, color = circleColor)
        Text("9", modifier = Modifier.align(Alignment.CenterStart).padding(start = 40.dp), fontSize = 14.sp, color = circleColor)

        // Center Text
        val centerText = if (tasks.isEmpty()) "empty" else "filled1"
        Text(centerText, color = centerTextColor, fontWeight = FontWeight.Medium)
    }
}
