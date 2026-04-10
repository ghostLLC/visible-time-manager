package com.vtm.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtm.app.components.ClockFace
import com.vtm.app.model.TimeTask
import com.vtm.app.ui.theme.ClockRed

@Composable
fun MainScreen() {
    var isNightMode by remember { mutableStateOf(false) }
    
    // Dummy tasks for visualization
    val tasks = remember {
        listOf(
            TimeTask(
                id = "1",
                title = "Design UI",
                type = "Work",
                startHour = 9,
                startMinute = 0,
                endHour = 12,
                endMinute = 0,
                color = ClockRed
            )
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Greeting
            Text(
                text = "问候语--根据当前任务设定",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Clock Face
            ClockFace(
                isNightMode = isNightMode,
                tasks = tasks,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = { /* Set Project */ }) {
                    Text("Set Project")
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Day", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isNightMode,
                        onCheckedChange = { isNightMode = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Night", fontSize = 12.sp)
                }
                
                OutlinedButton(onClick = { /* Setting */ }) {
                    Text("Setting")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Current Project Card
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("[Current Project] ends at hh:mm", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("x hour(s) x min(s) left", fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Next Project Card
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("[Next Project] begins at hh:mm", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("x hour(s) x min(s) left", fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Set Button (Bottom)
            OutlinedButton(
                onClick = { /* SET */ },
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("SET")
            }
        }
    }
}
