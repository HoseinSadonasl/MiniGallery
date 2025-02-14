package com.hotaku.media_datasource

import androidx.paging.PagingSource
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.model.MediaData
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.mapper.MapMediaEntityAsMediaData
import javax.inject.Inject

internal class MediaDataSourceImpl
    @Inject
    constructor(
        private val mapMediaEntityAsMediaData: MapMediaEntityAsMediaData,
        private val mediaDao: MediaDao,
    ) : MediaDataSource {
        override fun getMedia(
            mimeType: String,
            query: String,
            albumName: String,
        ): PagingSource<Int, MediaData> =
            MediaPagingSource(
                mapMediaEntityAsMediaData = mapMediaEntityAsMediaData,
                mediaDao = mediaDao,
                mimeType = mimeType,
                query = query,
                albumName = albumName,
            )
    }
