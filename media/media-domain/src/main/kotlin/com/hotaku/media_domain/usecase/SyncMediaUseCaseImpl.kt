package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.repository.SyncMediaRepository
import com.hotaku.media_domain.util.SyncDataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SyncMediaUseCaseImpl
    @Inject
    constructor(
        private val syncMediaRepository: SyncMediaRepository,
    ) : SyncMediaUseCase {
        override operator fun invoke(): Flow<SyncDataState> = syncMediaRepository.synchronize()
    }
