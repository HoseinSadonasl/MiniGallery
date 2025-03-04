package com.hotaku.database.entity

import androidx.room.Entity

@Entity(tableName = "album")
data class AlbumEntity(
    val displayName: String,
    val thumbnailUriString: String,
    val thumbnailType: String,
    val count: Int,
)
