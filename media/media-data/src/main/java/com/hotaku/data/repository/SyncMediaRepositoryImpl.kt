package com.hotaku.data.repository

import android.content.Context
import android.provider.MediaStore
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.worker.SyncWorker
import com.hotaku.data.worker.SyncWorker.Companion.synchronizeAndReturnResultFlow
import com.hotaku.datastore.MediaGenerationDataSource
import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.util.SyncDataState
import com.hotaku.media_domain.util.SyncFailureReason.LOW_STORAGE
import com.hotaku.media_domain.util.SyncFailureReason.UNKNOWN
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class SyncMediaRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val mediaGenerationDataSource: MediaGenerationDataSource,
        @Dispatcher(MiniGalleryDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
    ) : SyncMediaRepository {
        override fun synchronize(): Flow<SyncDataState> =
            flow {
                if (shouldSynchronizeMedia()) {
                    WorkManager.getInstance(context).synchronizeAndReturnResultFlow()
                        .map { workInfo ->
                            when (workInfo?.state) {
                                WorkInfo.State.SUCCEEDED -> {
                                    val count = workInfo.outputData.getInt(SyncWorker.MEDIA_COUNT_KEY, 0)
                                    SyncDataState.SyncSuccess(
                                        itemsCount = count,
                                    )
                                }
                                WorkInfo.State.RUNNING -> {
                                    SyncDataState.Syncing
                                }
                                WorkInfo.State.FAILED -> {
                                    val reason =
                                        if (workInfo.stopReason == WorkInfo.STOP_REASON_CONSTRAINT_STORAGE_NOT_LOW) LOW_STORAGE else UNKNOWN
                                    SyncDataState.SyncFailure(reason = reason)
                                }
                                else -> {
                                    SyncDataState.Idle
                                }
                            }
                        }
                        .catch {
                            it.printStackTrace()
                            emit(SyncDataState.SyncFailure(UNKNOWN))
                        }
                        .collect { emit(it) }
                } else {
                    emit(SyncDataState.Idle)
                }
            }

        private suspend fun shouldSynchronizeMedia(): Boolean =
            withContext(coroutineDispatcher) {
                val contentProviderGeneration =
                    MediaStore.getGeneration(context, MediaStore.VOLUME_EXTERNAL)
                val localGeneration = mediaGenerationDataSource.getMediaGeneration()
                val shouldSync = localGeneration < contentProviderGeneration
                if (shouldSync) mediaGenerationDataSource.setMediaGeneration(contentProviderGeneration.toInt())
                shouldSync
            }
    }
