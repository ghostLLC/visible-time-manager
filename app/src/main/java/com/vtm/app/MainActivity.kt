package com.vtm.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.vtm.app.ui.MainScreen
import com.vtm.app.ui.theme.VTMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isNightMode by rememberSaveable { mutableStateOf(false) }

            VTMTheme(darkTheme = isNightMode) {
                MainScreen(
                    isNightMode = isNightMode,
                    onNightModeChange = { isNightMode = it }
                )
            }
        }
    }
}
