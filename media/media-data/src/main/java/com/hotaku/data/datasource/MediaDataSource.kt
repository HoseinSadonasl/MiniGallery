package com.hotaku.data.datasource

import androidx.paging.PagingSource
import com.hotaku.data.model.MediaData

interface MediaDataSource {
    fun getMedia(
        mimeType: String,
        query: String,
        albumName: String,
    ): PagingSource<Int, MediaData>

    suspend fun updateMedia(mediaData: MediaData)

    suspend fun deleteMedia(mediaData: List<MediaData>)
}
