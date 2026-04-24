package com.vtm.app.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class TimeTask(
    val id: String,
    val title: String,
    val date: LocalDate,
    val startHour: Int, // 0-23
    val startMinute: Int, // 0-59
    val endHour: Int,
    val endMinute: Int,
    val color: Color
)
