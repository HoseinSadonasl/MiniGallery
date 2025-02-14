package com.hotaku.media.screens.albums

import androidx.paging.compose.LazyPagingItems
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi

internal sealed interface AlbumsScreenActions {
    data class OnAlbumClick(val album: AlbumUi) : AlbumsScreenActions

    data class OnOpenAlbum(val media: LazyPagingItems<MediaUi>) : AlbumsScreenActions

    data object OnCloseAlbum : AlbumsScreenActions
}
