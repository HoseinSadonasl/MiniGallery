package com.hotaku.media_datasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey
    val mediaId: String,
    val uriString: String,
    val displayName: String,
    val mimeType: String,
    val duration: String,
    val dateAdded: String,
    val dateModified: String,
    val size: String,
    val isTrash: Boolean,
    val isFavorite: Boolean,
    val bucketDisplayName: String,
)
