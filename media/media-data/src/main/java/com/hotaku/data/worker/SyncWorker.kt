package com.hotaku.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hotaku.domain.utils.DataResult
import com.hotaku.media_domain.repository.ProviderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val providerRepository: ProviderRepository,
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            withContext(dispatcher) {
                val updateResult = providerRepository.updateMediaDatabase().last()
                return@withContext if (updateResult is DataResult.Success) {
                    Result.success(
                        workDataOf(MEDIA_COUNT_KEY to updateResult.data),
                    )
                } else {
                    Result.failure()
                }
            }

        companion object {
            const val SYNC_MEDIA_WORKER_TAG = "SyncMediaWorkerTag"
            const val MEDIA_COUNT_KEY = "MediaCount"
            private val syncConstraints
                get() =
                    Constraints.Builder()
                        .setRequiresStorageNotLow(true)
                        .build()
            val initializeSynchronizer
                get() =
                    OneTimeWorkRequestBuilder<SyncWorker>()
                        .setBackoffCriteria(
                            androidx.work.BackoffPolicy.LINEAR,
                            1,
                            TimeUnit.MINUTES,
                        )
                        .setConstraints(syncConstraints)
                        .build()
        }
    }
