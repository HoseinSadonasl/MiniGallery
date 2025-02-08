package com.hotaku.data.repository

import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.data.mapper.MapAlbumsAsDomain
import com.hotaku.media_domain.model.Album
import com.hotaku.media_domain.repository.AlbumsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AlbumsRepositoryImpl
    @Inject
    constructor(
        private val albumsDataSource: AlbumsDataSource,
        private val mapAlbumsAsDomain: MapAlbumsAsDomain,
        @Dispatcher(MiniGalleryDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    ) : AlbumsRepository {
        override suspend fun getAlbums(): List<Album> =
            withContext(ioDispatcher) {
                albumsDataSource.getAlbums().map { mapAlbumsAsDomain.map(it) }
            }
    }
