package com.hotaku.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey
    val mediaId: String,
    val path: String,
    val displayName: String,
    val mimeType: String,
    val dateAdded: String,
    val dateModified: String,
    val size: String,
)
