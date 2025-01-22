package com.hotaku.data.repository

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.worker.SyncUtils.synchronize
import com.hotaku.data.worker.SyncWorker
import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.util.SyncDataState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SyncMediaRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        @Dispatcher(MiniGalleryDispatchers.IO) private val coroutineDispatcher: CoroutineDispatcher,
    ) : SyncMediaRepository {
        override fun synchronize(): Flow<SyncDataState> {
            return WorkManager.getInstance(context).run {
                synchronize()
                getWorkInfosByTagLiveData(SyncWorker.SYNC_MEDIA_WORKER_TAG).value?.asFlow()
                    ?.map { workInfo ->

                        when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> {
                                val count = workInfo.outputData.getInt(SyncWorker.MEDIA_COUNT_KEY, 0)
                                SyncDataState.SyncSuccess(
                                    itemsCount = count,
                                )
                            }

                            WorkInfo.State.FAILED -> {
                                SyncDataState.SyncFailure
                            }

                            else -> {
                                SyncDataState.Idle
                            }
                        }
                    } ?: emptyFlow()
            }.flowOn(coroutineDispatcher)
        }
    }
