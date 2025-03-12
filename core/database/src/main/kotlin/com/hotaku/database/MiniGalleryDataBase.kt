package com.hotaku.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hotaku.media_datasource.dao.MediaDao
import com.hotaku.media_datasource.entities.MediaEntity

@Database(
    entities = [MediaEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class MiniGalleryDataBase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}
