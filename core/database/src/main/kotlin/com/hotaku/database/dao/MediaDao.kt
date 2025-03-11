package com.hotaku.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hotaku.database.entity.AlbumEntity
import com.hotaku.database.entity.MediaEntity

@Dao
interface MediaDao {
    @Upsert
    fun upsertAll(media: List<MediaEntity>)

    @Upsert
    fun upsertMedia(media: MediaEntity)

    @Delete
    suspend fun deleteMedia(media: List<MediaEntity>)

    @Query(
        "SELECT * FROM media \n" +
            "WHERE (displayName LIKE '%' || :query || '%' OR :query IS NULL) " +
            "AND isTrash = 0 " +
            "AND (mimeType = :mimeType OR :mimeType IS NULL) " +
            "AND (bucketDisplayName LIKE '%' || :albumName || '%' OR :albumName IS NULL) " +
            "ORDER BY dateModified DESC " +
            "LIMIT :limit OFFSET :offset",
    )
    suspend fun getAll(
        query: String? = null,
        mimeType: String? = null,
        albumName: String? = null,
        limit: Int,
        offset: Int,
    ): List<MediaEntity>

    @Query("SELECT uriString FROM media")
    suspend fun getAllUris(): List<String>

    @Query("DELETE FROM media WHERE uriString IN (:uris)")
    fun deleteByUris(uris: List<String>)

    @Query(
        "SELECT" +
            " COUNT(*) AS count," +
            " bucketDisplayName AS displayName," +
            " MAX(uriString) AS thumbnailUriString," +
            " MAX(mimeType) AS thumbnailType" +
            " FROM media GROUP BY" +
            " bucketDisplayName ORDER BY bucketDisplayName ASC",
    )
    suspend fun getAlbums(): List<AlbumEntity>
}
