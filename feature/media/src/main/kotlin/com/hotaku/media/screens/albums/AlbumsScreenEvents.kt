package com.hotaku.media.screens.albums

internal sealed interface AlbumsScreenEvents {
    data class OnNavigateToMediaScreen(val albumName: String) : AlbumsScreenEvents
}
