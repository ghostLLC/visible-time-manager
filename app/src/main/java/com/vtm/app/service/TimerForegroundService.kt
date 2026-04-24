package com.vtm.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.vtm.app.MainActivity
import com.vtm.app.R

class TimerForegroundService : Service() {

    companion object {
        const val CHANNEL_ID = "vtm_timer_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START = "com.vtm.app.action.START_TIMER"
        const val ACTION_STOP = "com.vtm.app.action.STOP_TIMER"
        const val EXTRA_TASK_TITLE = "task_title"
        const val EXTRA_REMAINING = "remaining_minutes"

        fun start(context: Context, taskTitle: String, remainingMinutes: Int) {
            val intent = Intent(context, TimerForegroundService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_TASK_TITLE, taskTitle)
                putExtra(EXTRA_REMAINING, remainingMinutes)
            }
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, TimerForegroundService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    private var currentTitle: String = ""
    private var currentRemaining: Int = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }

        currentTitle = intent?.getStringExtra(EXTRA_TASK_TITLE) ?: "Timer"
        currentRemaining = intent?.getIntExtra(EXTRA_REMAINING, 0) ?: 0

        startForeground(NOTIFICATION_ID, buildNotification())
        return START_NOT_STICKY
    }

    fun updateNotification(taskTitle: String, remainingMinutes: Int) {
        currentTitle = taskTitle
        currentRemaining = remainingMinutes
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, buildNotification())
    }

    private fun buildNotification(): Notification {
        val openIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val remainingText = if (currentRemaining > 0) {
            val h = currentRemaining / 60
            val m = currentRemaining % 60
            when {
                h > 0 && m > 0 -> "${h}h ${m}m left"
                h > 0 -> "${h}h left"
                else -> "${m}m left"
            }
        } else ""

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentTitle)
            .setContentText(remainingText)
            .setSmallIcon(R.drawable.ic_timer_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = "Shows active timer progress"
                setShowBadge(false)
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
    }
}
