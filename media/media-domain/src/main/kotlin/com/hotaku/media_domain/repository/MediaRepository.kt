package com.hotaku.media_domain.repository

import androidx.paging.PagingData
import com.hotaku.media_domain.models.Media
import kotlinx.coroutines.flow.Flow
import kotlin.Boolean

interface MediaRepository {
    fun getMedia(
        initialKey: Int,
        mimeType: String,
        query: String,
        albumName: String,
        matchTrash: Boolean,
        matchFavorite: Boolean,
    ): Flow<PagingData<Media>>

    suspend fun renameMedia(media: Media): Boolean

    suspend fun trashMedia(media: List<Media>): Boolean

    suspend fun markMediaAsFavorite(media: Media): Boolean

    suspend fun deleteMedia(media: List<Media>): Boolean
}
