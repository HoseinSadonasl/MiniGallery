package com.hotaku.media_domain.repository

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import kotlinx.coroutines.flow.Flow

interface ProviderRepository {
    suspend fun updateMediaDatabase(): Flow<DataResult<Int, Error>>
}
