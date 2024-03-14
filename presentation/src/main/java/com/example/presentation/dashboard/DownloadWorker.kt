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
import android.util.Log
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
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        setForeground(
            createForegroundInfo(
                title = "FRANKFURTER",
                text = "Loading dashboard items..."
            )
        )
        Log.d("k0dm", "DownloadWorker do the thing")
        val repository = EntryPointAccessors.fromApplication(
            context,
            ProvideDashboardRepositoryEntryPoint::class.java
        ).repository()
        val mapper = EntryPointAccessors.fromApplication(
            context,
            ProvideDashboardMapperEntryPoint::class.java
        ).mapper()
        val result = repository.dashboards()
        Log.d("k0dm", result.toString())

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

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProvideDashboardRepositoryEntryPoint {

    fun repository(): DashboardRepository
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProvideDashboardMapperEntryPoint {

    fun mapper(): DashboardResult.Mapper
}