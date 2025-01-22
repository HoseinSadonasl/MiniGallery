package com.hotaku.media_domain.usecase

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.ErrorResult
import com.hotaku.domain.utils.executeFlowResult
import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.util.SyncDataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class SyncMediaUseCaseImpl
    @Inject
    constructor(
        private val syncMediaRepository: SyncMediaRepository,
    ) : SyncMediaUseCase {
        override operator fun invoke(): Flow<DataResult<Int, Error>> =
            flow {
                syncMediaRepository.synchronize().onEach { result ->
                    when (result) {
                        SyncDataState.Idle -> Unit
                        SyncDataState.SyncFailure -> {
                            emit(DataResult.Failure(ErrorResult.LocalError.SYNC_DATA_ERROR))
                        }

                        is SyncDataState.SyncSuccess -> {
                            emit(DataResult.Success(result.itemsCount))
                        }
                    }
                }
            }.executeFlowResult()
    }
