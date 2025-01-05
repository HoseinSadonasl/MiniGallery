package com.hotaku.media_domain.repository

import androidx.paging.PagingData
import com.hotaku.domain.utils.DataResult
import com.hotaku.domain.utils.Error
import com.hotaku.media_domain.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun synchronizeMedia(): Flow<DataResult<Int, Error>>

    fun getMedia(
        mimeType: String? = null,
        query: String? = null,
    ): Flow<PagingData<Media>>
}
