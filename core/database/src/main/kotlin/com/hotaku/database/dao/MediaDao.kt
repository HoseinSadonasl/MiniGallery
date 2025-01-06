package com.hotaku.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hotaku.database.entity.MediaEntity

@Dao
interface MediaDao {
    @Upsert
    fun insertAll(media: List<MediaEntity>)

    @Query("SELECT * FROM media")
    fun getAll(): List<MediaEntity>
}
