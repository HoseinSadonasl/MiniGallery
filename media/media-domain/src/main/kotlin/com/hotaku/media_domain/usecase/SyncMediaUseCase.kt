package com.hotaku.media_domain.usecase

import com.hotaku.media_domain.util.SyncDataState
import kotlinx.coroutines.flow.Flow

interface SyncMediaUseCase {
    operator fun invoke(): Flow<SyncDataState>
}
