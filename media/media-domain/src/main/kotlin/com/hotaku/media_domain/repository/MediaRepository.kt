package com.hotaku.media_domain.repository

import androidx.paging.PagingData
import com.hotaku.media_domain.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getMedia(
        mimeType: String,
        query: String,
        albumName: String,
    ): Flow<PagingData<Media>>

    suspend fun updateMedia(media: Media)

    suspend fun deleteMediaById(mediaUriString: String)
}
