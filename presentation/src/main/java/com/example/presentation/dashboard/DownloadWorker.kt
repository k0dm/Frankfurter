package com.example.presentation.dashboard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.domain.dashboard.DashboardRepository
import com.example.domain.dashboard.DashboardResult
import com.example.presentation.R
import com.example.presentation.main.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted parameters: WorkerParameters,
    private val repository: DashboardRepository,
    private val mapper: DashboardResult.Mapper
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        setForeground(
            createForegroundInfo(
                title = context.getString(R.string.title),
                text = context.getString(R.string.loading_dashboard_items)
            )
        )
        val result = repository.downloadDashboards()

        withContext(Dispatchers.Main) {
            result.map(mapper)
        }

        return Result.success()
    }

    private fun notification(title: String, text: String): Notification {

        val name = "DownloadWorkManager Notification"
        val description = "Do the thing"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("VERBOSE_NOTIFICATION", name, importance)
        channel.description = description

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val resulPendingIntent = TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(resultIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(applicationContext, "VERBOSE_NOTIFICATION")
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.settings_icon)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(resulPendingIntent)
            .build()
    }

    private fun createForegroundInfo(title: String, text: String): ForegroundInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ForegroundInfo(9999999, notification(title, text), FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else ForegroundInfo(9999999, notification(title, text))
}