package com.hotaku.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hotaku.data.datasource.MediaDataSource
import com.hotaku.data.mapper.MapMediaAsDomain
import com.hotaku.media_domain.model.Media
import com.hotaku.media_domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MediaRepositoryImpl
    @Inject
    constructor(
        private val mediaDataSource: MediaDataSource,
        private val mediaAsDomain: MapMediaAsDomain,
    ) : MediaRepository {
        override fun getMedia(
            mimeType: String?,
            query: String?,
        ): Flow<PagingData<Media>> =
            Pager(
                config =
                    PagingConfig(
                        initialLoadSize = INITIAL_LOAD_SIZE,
                        pageSize = PAGE_SIZE,
                    ),
                pagingSourceFactory = {
                    mediaDataSource.getMedia(
                        mimeType = mimeType,
                        query = query,
                    )
                },
            ).flow.map { data -> data.map { mediaData -> mediaAsDomain.map(mediaData) } }

        companion object {
            private const val INITIAL_LOAD_SIZE = 40
            private const val PAGE_SIZE = 30
        }
    }
