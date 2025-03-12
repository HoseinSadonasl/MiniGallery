package com.hotaku.media_datasource.entities

import androidx.room.Entity

@Entity(tableName = "album")
data class AlbumEntity(
    val displayName: String,
    val thumbnailUriString: String,
    val thumbnailType: String,
    val count: Int,
)
