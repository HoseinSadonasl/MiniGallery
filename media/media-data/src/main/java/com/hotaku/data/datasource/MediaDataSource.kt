package com.hotaku.data.datasource

import androidx.paging.PagingData
import com.hotaku.data.modes.MediaData
import kotlinx.coroutines.flow.Flow

interface MediaDataSource {
    fun getMedia(
        mimeType: String,
        query: String,
        albumName: String,
        matchTrash: Boolean,
        matchFavorite: Boolean,
    ): Flow<PagingData<MediaData>>

    suspend fun updateMedia(mediaData: MediaData)

    suspend fun deleteMedia(mediaData: List<MediaData>)
}
