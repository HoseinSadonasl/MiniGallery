package com.hotaku.media_domain.repository

import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import kotlinx.coroutines.flow.Flow

interface SyncMediaRepository {
    fun synchronize(): Flow<DataResult<Int, Error>>
}
