package com.hotaku.database.di

import android.content.Context
import androidx.room.Room
import com.hotaku.database.MiniGalleryDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesMiniGalleryDatabase(
        @ApplicationContext context: Context,
    ): MiniGalleryDataBase =
        Room.databaseBuilder(
            context = context,
            klass = MiniGalleryDataBase::class.java,
            name = "mini-gallery-db",
        ).build()
}
