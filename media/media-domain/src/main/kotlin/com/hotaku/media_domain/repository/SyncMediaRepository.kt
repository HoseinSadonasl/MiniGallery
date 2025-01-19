package com.hotaku.media_domain.repository

import com.hotaku.media_domain.util.SyncDataState
import kotlinx.coroutines.flow.Flow

interface SyncMediaRepository {
    fun synchronize(): Flow<SyncDataState>
}
