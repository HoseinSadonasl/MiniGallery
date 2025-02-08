package com.hotaku.media.screens.shared

import androidx.paging.PagingData
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi

internal sealed interface SharedActions {
    data class OnUpdateMedia(val media: PagingData<MediaUi>) : SharedActions

    data class OnUpdateAlbums(val albums: List<AlbumUi>) : SharedActions
}
