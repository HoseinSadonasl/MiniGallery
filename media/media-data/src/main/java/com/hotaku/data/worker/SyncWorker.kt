package com.hotaku.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.domain.utils.DataResult
import com.hotaku.media_domain.repository.ProviderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
internal class SyncWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val providerRepository: ProviderRepository,
        @Dispatcher(MiniGalleryDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
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
                            BackoffPolicy.LINEAR,
                            1,
                            TimeUnit.MINUTES,
                        )
                        .setConstraints(syncConstraints)
                        .build()
        }
    }
