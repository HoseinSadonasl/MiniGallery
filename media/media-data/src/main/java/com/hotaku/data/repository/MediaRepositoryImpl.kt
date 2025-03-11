package com.hotaku.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.datasource.UpdateMediaContentProviderDataSource
import com.hotaku.data.mapper.MapMediaAsMediaData
import com.hotaku.data.mapper.MapMediaDataAsMedia
import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MediaRepositoryImpl
    @Inject
    constructor(
        private val mediaDataSource: MediaDataSource,
        private val updateMediaContentProviderDataSource: UpdateMediaContentProviderDataSource,
        private val mapMediaDataAsMedia: MapMediaDataAsMedia,
        private val mapMediaAsMediaData: MapMediaAsMediaData,
        @Dispatcher(MiniGalleryDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
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
            ).flow.map { data -> data.map { mediaData -> mapMediaDataAsMedia.map(from = mediaData) } }

        override suspend fun renameMedia(media: Media): Boolean =
            withContext(NonCancellable) {
                updateMediaContentProviderDataSource.renameMedia(
                    mediaUriString = media.uriString,
                    name = media.displayName,
                ).let { isSccess ->
                    if (isSccess) {
                        withContext(ioDispatcher) {
                            mediaDataSource.updateMedia(mapMediaAsMediaData.map(from = media))
                        }
                    }
                    isSccess
                }
            }

        override suspend fun trashMedia(media: List<Media>): Boolean =
            withContext(NonCancellable) {
                media.onEach { mediaData ->
                    withContext(ioDispatcher) {
                        mediaDataSource.updateMedia(mapMediaAsMediaData.map(from = mediaData))
                    }
                }.filterNot { it.isTrash }.isEmpty()
            }

        companion object {
            private const val INITIAL_LOAD_SIZE = 40
            private const val PAGE_SIZE = 40
        }
    }
