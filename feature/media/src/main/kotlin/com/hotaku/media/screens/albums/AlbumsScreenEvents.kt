package com.hotaku.media.screens.albums

internal sealed interface AlbumsScreenEvents {
    data object OnNavigateToMediaPane : AlbumsScreenEvents
}
