package com.hotaku.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.mapper.MapMediaAsMediaData
import com.hotaku.data.mapper.MapMediaDataAsMedia
import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MediaRepositoryImpl
    @Inject
    constructor(
        private val mediaDataSource: MediaDataSource,
        private val mapMediaDataAsMedia: MapMediaDataAsMedia,
        private val mapMediaAsMediaData: MapMediaAsMediaData,
    ) : MediaRepository {
        override fun getMedia(
            mimeType: String,
            query: String,
            albumName: String,
        ): Flow<PagingData<Media>> =
            Pager(
                config =
                    PagingConfig(
                        initialLoadSize = INITIAL_LOAD_SIZE,
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    mediaDataSource.getMedia(
                        mimeType = mimeType,
                        query = query,
                        albumName = albumName,
                    )
                },
            ).flow.map { data -> data.map { mediaData -> mapMediaDataAsMedia.map(mediaData) } }

        override suspend fun updateMedia(media: Media) =
            withContext(NonCancellable) {
                mapMediaAsMediaData.map(media).let { mediaData ->
                    mediaDataSource.updateMedia(mediaData = mediaData)
                }
            }

        override suspend fun deleteMedia(media: List<Media>) =
            withContext(NonCancellable) {
                media.map { mapMediaAsMediaData.map(it) }.let { mediaData ->
                    mediaDataSource.deleteMedia(mediaData = mediaData)
                }
            }

        companion object {
            private const val INITIAL_LOAD_SIZE = 40
            private const val PAGE_SIZE = 40
        }
    }
