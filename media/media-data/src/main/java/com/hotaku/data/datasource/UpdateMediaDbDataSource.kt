package com.hotaku.data.datasource

import com.hotaku.data.model.MediaData

interface UpdateMediaDbDataSource {
    suspend fun saveMedia(media: List<MediaData>)
}
