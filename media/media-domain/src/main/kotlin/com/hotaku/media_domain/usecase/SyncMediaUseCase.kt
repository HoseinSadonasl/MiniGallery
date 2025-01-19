package com.hotaku.media_domain.usecase

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import kotlinx.coroutines.flow.Flow

interface SyncMediaUseCase {
    operator fun invoke(): Flow<DataResult<Int, Error>>
}
