package com.hotaku.media.screens.albums

import com.hotaku.media.model.AlbumUi

internal sealed interface AlbumsScreenEvents {
    data class OnNavigateToWithAlbum(val albumUi: AlbumUi) : AlbumsScreenEvents
}
