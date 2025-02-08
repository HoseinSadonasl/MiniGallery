package com.hotaku.media_domain.repository

import com.hotaku.media_domain.model.Album

interface AlbumsRepository {
    suspend fun getAlbums(): List<Album>
}
