package com.hotaku.media_datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.modes.MediaData
import com.hotaku.media_datasource.dao.MediaDao
import com.hotaku.media_datasource.mappers.MapMediaDataAsMediaEntity
import com.hotaku.media_datasource.mappers.MapMediaEntityAsMediaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MediaDataSourceImpl
    @Inject
    constructor(
        private val mapMediaEntityAsMediaData: MapMediaEntityAsMediaData,
        private val mapMediaDataAsMediaEntity: MapMediaDataAsMediaEntity,
        private val mediaDao: MediaDao,
    ) : MediaDataSource {
        override fun getMedia(
            initialKey: Int,
            mimeType: String,
            query: String,
            albumName: String,
            matchTrash: Boolean,
            matchFavorite: Boolean,
        ): Flow<PagingData<MediaData>> =
            Pager(
                config = pagingConfig,
                initialKey = initialKey,
                pagingSourceFactory = {
                    mediaDao.getAll(
                        mimeType = mimeType,
                        query = query,
                        albumName = albumName,
                        isTrash = matchTrash,
                        isFavorite = matchFavorite,
                    )
                },
            ).flow.map { pagingData ->
                pagingData.map { mediaEntity ->
                    mapMediaEntityAsMediaData.map(from = mediaEntity)
                }
            }

        override suspend fun updateMedia(mediaData: MediaData) =
            mapMediaDataAsMediaEntity.map(from = mediaData)
                .let { mediaEntity -> mediaDao.upsertMedia(media = mediaEntity) }

        override suspend fun deleteMedia(mediaData: List<MediaData>): Int =
            mediaData.map { mapMediaDataAsMediaEntity.map(from = it) }
                .let { mediaDao.deleteMedia(media = it) }

        private val pagingConfig: PagingConfig
            get() =
                PagingConfig(
                    initialLoadSize = INITIAL_LOAD_SIZE,
                    pageSize = PAGE_SIZE,
                )

        private companion object {
            private const val INITIAL_LOAD_SIZE = 40
            private const val PAGE_SIZE = 20
        }
    }
