package com.hotaku.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hotaku.database.dao.MediaDao
import com.hotaku.database.entity.MediaEntity

@Database(
    entities = [MediaEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class MiniGalleryDataBase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}
