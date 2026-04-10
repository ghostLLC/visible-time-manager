package com.vtm.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vtm.app.ui.MainScreen
import com.vtm.app.ui.theme.VTMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VTMTheme {
                MainScreen()
            }
        }
    }
}
