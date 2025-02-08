package com.hotaku.data.di

import com.hotaku.common.di.Dispatcher
import com.hotaku.common.di.MiniGalleryDispatchers
import com.hotaku.data.datasource.AlbumsDataSource
import com.hotaku.data.mapper.MapAlbumAsDomain
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
        mapAlbumAsDomain: MapAlbumAsDomain = MapAlbumAsDomain(),
        @Dispatcher(MiniGalleryDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): AlbumsRepository =
        AlbumsRepositoryImpl(
            albumsDataSource = albumsDataSource,
            mapAlbumAsDomain = mapAlbumAsDomain,
            ioDispatcher = ioDispatcher,
        )
}
