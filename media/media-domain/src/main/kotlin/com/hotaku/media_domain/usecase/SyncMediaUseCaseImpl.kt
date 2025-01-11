package com.hotaku.media_domain.usecase

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.domain.utils.executeFlowResult
import com.hotaku.media_domain.repository.SyncMediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class SyncMediaUseCaseImpl
    @Inject
    constructor(
        private val syncMediaRepository: SyncMediaRepository,
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : SyncMediaUseCase {
        override suspend operator fun invoke(): Flow<DataResult<Int, Error>> =
            flow {
                syncMediaRepository.synchronize().onEach { result ->
                    if (result is DataResult.Success) {
                        emit(DataResult.Success(result.data))
                    }
                }.executeFlowResult(coroutineDispatcher = dispatcher)
            }
    }
