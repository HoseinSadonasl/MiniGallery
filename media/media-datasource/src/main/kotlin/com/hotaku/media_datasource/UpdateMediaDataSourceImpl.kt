package com.hotaku.media_datasource

import com.hotaku.data.datasource.UpdateMediaDbDataSource
import com.hotaku.data.model.MediaData
import com.hotaku.database.dao.MediaDao
import com.hotaku.media_datasource.mapper.MapMediaDataAsMediaEntity
import javax.inject.Inject

internal class UpdateMediaDataSourceImpl
    @Inject
    constructor(
        private val mediaDao: MediaDao,
        private val mapMediaDataAsMediaEntity: MapMediaDataAsMediaEntity,
    ) : UpdateMediaDbDataSource {
        override suspend fun getMediaStringUris(): List<String> = mediaDao.getAllUris()

        override fun deleteNoExistMedia(uris: List<String>) = mediaDao.deleteByUris(uris)

        override fun insertMediaToDatabase(media: List<MediaData>) {
            media.map { mapMediaDataAsMediaEntity.map(it) }.also {
                mediaDao.insertAll(it)
            }
        }
    }
