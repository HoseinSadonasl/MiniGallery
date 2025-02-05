package com.hotaku.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.media_domain.repository.UpdateLocalMediaRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.TimeUnit

@HiltWorker
internal class SyncWorker
    @AssistedInject
    constructor(
        @Assisted private val appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val updateLocalMediaRepository: UpdateLocalMediaRepository,
        @Dispatcher(MiniGalleryDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            withContext(coroutineDispatcher) {
                return@withContext try {
                    updateLocalMediaRepository.update().getOrNull()?.let {
                        Result.success(
                            workDataOf(MEDIA_COUNT_KEY to it),
                        )
                    } ?: Result.failure()
                } catch (exception: IOException) {
                    exception.printStackTrace()
                    Result.failure()
                }
            }

        companion object {
            const val MEDIA_COUNT_KEY = "MediaCount"
            private val syncConstraints
                get() =
                    Constraints.Builder()
                        .setRequiresStorageNotLow(true)
                        .build()
            private val initializeSynchronizerWorkRequest
                get() =
                    OneTimeWorkRequestBuilder<SyncWorker>()
                        .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            30,
                            TimeUnit.SECONDS,
                        )
                        .setConstraints(syncConstraints)
                        .build()

            fun WorkManager.synchronizeAndReturnResultFlow(): Flow<WorkInfo?> {
                val syncWorkRequest = initializeSynchronizerWorkRequest
                enqueue(syncWorkRequest)
                return getWorkInfoByIdFlow(syncWorkRequest.id)
            }
        }
    }
