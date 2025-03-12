package com.hotaku.data.datasource

import com.hotaku.data.modes.AlbumData

interface AlbumsDataSource {
    suspend fun getAlbums(): List<AlbumData>
}
