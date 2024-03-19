package com.example.presentation.dashboard

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.example.domain.dashboard.ForegroundDownloadWorkManagerWrapper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BaseForegroundDownloadWorkManagerWrapper @Inject constructor(
    @ApplicationContext private val context: Context
) : ForegroundDownloadWorkManagerWrapper {

    override fun start() {
        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "Download dashboard items",
            ExistingWorkPolicy.KEEP,
            downloadWorkRequest
        )
    }
}