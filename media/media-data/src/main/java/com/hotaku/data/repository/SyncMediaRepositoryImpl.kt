package com.hotaku.data.repository

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hotaku.data.worker.SyncWorker
import com.hotaku.data.worker.SyncWorker.Companion.synchronizeAndReturnResultFlow
import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.util.SyncDataState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SyncMediaRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : SyncMediaRepository {
        override fun synchronize(): Flow<SyncDataState> =
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
                            SyncDataState.SyncFailure
                        }

                        else -> {
                            SyncDataState.Idle
                        }
                    }
                }
    }
