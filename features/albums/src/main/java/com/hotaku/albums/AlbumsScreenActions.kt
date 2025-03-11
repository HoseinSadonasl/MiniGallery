package com.hotaku.albums

import com.hotaku.albums.model.AlbumUi

internal sealed interface AlbumsScreenActions {
    data object OnUpdateMediaList : AlbumsScreenActions

    data class OnAlbumClick(val album: AlbumUi) : AlbumsScreenActions

    data object OnClearSelectedAlbum : AlbumsScreenActions

    data class OnMediaItemClick(val mediaItemIndex: Int) : AlbumsScreenActions
}
