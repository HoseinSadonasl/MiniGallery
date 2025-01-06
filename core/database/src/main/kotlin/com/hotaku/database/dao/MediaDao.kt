package com.hotaku.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hotaku.database.entity.MediaEntity

@Dao
interface MediaDao {
    @Upsert
    fun insertAll(media: List<MediaEntity>)

    @Query("SELECT * FROM media WHERE (displayName = :query OR :query is NULL) AND (mimeType = :mimeType OR :mimeType is NULL)")
    fun getAll(
        query: String? = null,
        mimeType: String? = null,
    ): List<MediaEntity>
}
