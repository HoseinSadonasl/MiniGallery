package com.hotaku.albums

internal sealed interface AlbumsScreenEvents {
    data object OnOpenAlbum : AlbumsScreenEvents

    data object OnNavigateToMediaDetailScreen : AlbumsScreenEvents
}
