package com.hotaku.media_domain.repository

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import kotlinx.coroutines.flow.Flow

interface SyncMediaRepository {
    suspend fun synchronize(): Flow<DataResult<Int, Error>>
}
