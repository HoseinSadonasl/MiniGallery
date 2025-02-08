package com.hotaku.media.screens.albums

import com.hotaku.media.model.AlbumUi

internal sealed interface AlbumsScreenActions {
    data class OnUpdateAlbums(val albums: List<AlbumUi>) : AlbumsScreenActions

    data class OnAlbumClick(val albumName: String) : AlbumsScreenActions
}
