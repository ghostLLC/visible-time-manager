package com.vtm.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vtm.app.ui.MainScreen
import com.vtm.app.ui.theme.VTMTheme
import java.time.LocalTime
import kotlinx.coroutines.delay

private const val StartupWarmupDurationMillis = 360L

private fun defaultNightModeForNow(): Boolean {
    val currentHour = LocalTime.now().hour
    return currentHour < 6 || currentHour >= 18
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isNightMode by rememberSaveable { mutableStateOf(defaultNightModeForNow()) }
            var isStartupWarmupActive by rememberSaveable { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                withFrameNanos { }
                delay(StartupWarmupDurationMillis)
                isStartupWarmupActive = false
            }

            VTMTheme(darkTheme = isNightMode) {
                Box(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        isNightMode = isNightMode,
                        onNightModeChange = { isNightMode = it },
                        startupWarmupActive = isStartupWarmupActive,
                    )

                    AnimatedVisibility(
                        visible = isStartupWarmupActive,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        StartupWarmupOverlay()
                    }
                }
            }
        }
    }
}

@Composable
private fun StartupWarmupOverlay() {
    val backgroundColor = MaterialTheme.colorScheme.background
    val accentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.82f)
    val secondaryDotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.22f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .offset(x = (-12).dp)
                .clip(CircleShape)
                .background(secondaryDotColor),
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accentColor),
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .offset(x = 12.dp)
                .clip(CircleShape)
                .background(secondaryDotColor),
        )
    }
}
