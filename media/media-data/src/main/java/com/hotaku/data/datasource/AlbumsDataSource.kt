package com.hotaku.data.datasource

import com.hotaku.data.model.AlbumData

interface AlbumsDataSource {
    suspend fun getAlbums(): List<AlbumData>
}
