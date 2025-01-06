package com.hotaku.database.di

import com.hotaku.database.MiniGalleryDataBase
import com.hotaku.database.dao.MediaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesMediaDao(databse: MiniGalleryDataBase): MediaDao = databse.mediaDao()
}
