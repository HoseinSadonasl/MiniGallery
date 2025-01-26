package com.hotaku.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hotaku.database.entity.MediaEntity

@Dao
interface MediaDao {
    @Upsert
    fun insertAll(media: List<MediaEntity>)

    @Query(
        "SELECT * FROM media \n" +
            "WHERE (displayName LIKE '%' || :query || '%' OR :query IS NULL) " +
            "AND (mimeType = :mimeType OR :mimeType IS NULL) " +
            "ORDER BY dateModified DESC " +
            "LIMIT :limit OFFSET :offset",
    )
    suspend fun getAll(
        query: String? = null,
        mimeType: String? = null,
        limit: Int,
        offset: Int,
    ): List<MediaEntity>
}
