package com.hotaku.data.worker

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object SyncUtils {
    private const val SYNC_WORK_NAME = "SyncAllMedia"

    fun WorkManager.synchronize() {
        enqueueUniqueWork(
            SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            SyncWorker.initializeSynchronizer,
        )
    }
}
