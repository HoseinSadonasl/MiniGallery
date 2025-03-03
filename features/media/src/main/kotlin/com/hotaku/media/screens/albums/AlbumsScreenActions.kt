package com.hotaku.media.screens.albums

import com.hotaku.media.model.AlbumUi

internal sealed interface AlbumsScreenActions {
    data class OnAlbumClick(val album: AlbumUi) : AlbumsScreenActions

    data object OnCloseAlbum : AlbumsScreenActions

    data class OnMediaItemClick(val mediaItemIndex: Int) : AlbumsScreenActions
}
