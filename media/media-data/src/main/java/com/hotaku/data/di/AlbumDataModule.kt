package com.hotaku.data.di

import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.data.mappers.MapAlbumDataAsAlbum
import com.hotaku.data.repository.AlbumsRepositoryImpl
import com.hotaku.media_domain.repository.AlbumsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AlbumDataModule {
    @Provides
    @Singleton
    fun providesAlbumsRepository(
        albumsDataSource: AlbumsDataSource,
        mapAlbumDataAsAlbum: MapAlbumDataAsAlbum = MapAlbumDataAsAlbum(),
        @Dispatcher(MiniGalleryDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): AlbumsRepository =
        AlbumsRepositoryImpl(
            albumsDataSource = albumsDataSource,
            mapAlbumDataAsAlbum = mapAlbumDataAsAlbum,
            ioDispatcher = ioDispatcher,
        )
}
