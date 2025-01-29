package com.hotaku.data.datasource

import com.hotaku.data.model.MediaData

interface UpdateMediaDbDataSource {
    suspend fun getMediaStringUris(): List<String>

    fun deleteNoExistMedia(uris: List<String>)

    fun insertMediaToDatabase(media: List<MediaData>)
}
