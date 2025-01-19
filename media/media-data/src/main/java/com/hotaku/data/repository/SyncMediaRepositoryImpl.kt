package com.hotaku.data.repository

import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hotaku.data.worker.SyncUtils.synchronize
import com.hotaku.data.worker.SyncWorker
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.media_domain.repository.SyncMediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SyncMediaRepositoryImpl
    @Inject
    constructor(
        private val workManager: WorkManager,
    ) : SyncMediaRepository {
        override fun synchronize(): Flow<DataResult<Int, Error>> {
            workManager.synchronize()
            return workManager.getWorkInfosByTagLiveData(SyncWorker.SYNC_MEDIA_WORKER_TAG).value?.asFlow()
                ?.map { workInfo ->
                    when (workInfo.state) {
                        WorkInfo.State.RUNNING, WorkInfo.State.ENQUEUED, WorkInfo.State.BLOCKED -> {
                            DataResult.Loading
                        }

                        WorkInfo.State.SUCCEEDED -> {
                            DataResult.Success(
                                data =
                                    workInfo.outputData.getInt(
                                        SyncWorker.MEDIA_COUNT,
                                        0,
                                    ),
                            )
                        }

                        WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> {
                            DataResult.Failure(ErrorResult.UnknownError)
                        }
                    }
                } ?: emptyList<DataResult<Int, ErrorResult>>().asFlow()
        }
    }
