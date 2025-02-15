package com.hotaku.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.datasource.ProviderDataSource
import com.hotaku.data.mapper.MapMediaAsData
import com.hotaku.data.mapper.MapMediaAsDomain
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
        private val providerDataSource: ProviderDataSource,
        private val mediaAsDomain: MapMediaAsDomain,
        private val mapMediaAsData: MapMediaAsData,
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
            ).flow.map { data -> data.map { mediaData -> mediaAsDomain.map(mediaData) } }

        override suspend fun updateMedia(media: Media) {
            withContext(NonCancellable) {
                mapMediaAsData.map(media).let { mediaData ->
                    providerDataSource.updateMedia(media = mediaData).getOrThrow()
                }
            }
        }

        override suspend fun deleteMediaById(mediaUriString: String) {
            withContext(NonCancellable) {
                providerDataSource.deleteMediaByUri(mediaUriString = mediaUriString).getOrThrow()
            }
        }

        companion object {
            private const val INITIAL_LOAD_SIZE = 40
            private const val PAGE_SIZE = 40
        }
    }
