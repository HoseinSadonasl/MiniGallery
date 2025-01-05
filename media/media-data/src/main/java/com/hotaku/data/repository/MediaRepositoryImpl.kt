package com.hotaku.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.mapper.MapMediaAsDomain
import com.hotaku.data.worker.SyncWorker
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MediaRepositoryImpl
    @Inject
    constructor(
        private val mediaDataSource: MediaDataSource,
        private val mediaAsDomain: MapMediaAsDomain,
        private val workManager: WorkManager,
    ) : MediaRepository {
        override suspend fun synchronizeMedia(): Flow<DataResult<Int, Error>> {
            workManager.apply {
                enqueueUniqueWork(
                    SYNC_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    SyncWorker.initializeSynchronizer,
                )
            }
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

        override fun getMedia(
            mimeType: String?,
            query: String?,
        ): Flow<PagingData<Media>> =
            Pager(
                config =
                    PagingConfig(
                        initialLoadSize = INITIAL_LOAD_SIZE,
                        pageSize = PAGE_SIZE,
                    ),
                pagingSourceFactory = {
                    mediaDataSource.getMedia(
                        mimeType = mimeType,
                        query = query,
                    )
                },
            ).flow.map { data -> data.map { mediaData -> mediaAsDomain.map(mediaData) } }

        companion object {
            private const val SYNC_WORK_NAME = "SyncAllMedia"

            private const val INITIAL_LOAD_SIZE = 40
            private const val PAGE_SIZE = 30
        }
    }
