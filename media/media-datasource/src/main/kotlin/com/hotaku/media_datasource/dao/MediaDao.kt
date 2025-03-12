package com.hotaku.media_datasource.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hotaku.media_datasource.entities.AlbumEntity
import com.hotaku.media_datasource.entities.MediaEntity

@Dao
interface MediaDao {
    @Upsert
    fun upsertAll(media: List<MediaEntity>)

    @Upsert
    fun upsertMedia(media: MediaEntity)

    @Delete
    suspend fun deleteMedia(media: List<MediaEntity>)

    @Query(
        "SELECT * FROM media " +
            "WHERE (displayName LIKE '%' || :query || '%' OR :query IS NULL) " +
            "AND (mimeType LIKE '%' || :mimeType || '%' OR :mimeType IS NULL) " +
            "AND (bucketDisplayName LIKE '%' || :albumName || '%' OR :albumName IS NULL)" +
            "AND isTrash = :isTrash " +
            "AND isFavorite = :isFavorite " +
            "ORDER BY dateModified DESC",
    )
    fun getAll(
        query: String?,
        mimeType: String?,
        albumName: String?,
        isTrash: Int,
        isFavorite: Int,
    ): PagingSource<Int, MediaEntity>

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
            " FROM media WHERE isTrash = 0 " +
            "GROUP BY bucketDisplayName ORDER BY bucketDisplayName ASC",
    )
    suspend fun getAlbums(): List<AlbumEntity>
}
