package com.hotaku.albums

internal sealed interface AlbumsScreenEvents {
    data object OnNavigateToMediaDetailScreen : AlbumsScreenEvents
}
