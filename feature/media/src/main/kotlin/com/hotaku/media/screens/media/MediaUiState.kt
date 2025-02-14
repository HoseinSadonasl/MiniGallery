package com.hotaku.media.screens.media

import android.os.Parcelable
import com.hotaku.media.model.AlbumUi
import com.hotaku.media.model.MediaUi
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isTopBarVisible: Boolean = true,
    val selectedAlbum: AlbumUi? = null,
    val selectedMedia: MediaUi? = null,
    val mimeType: String = "",
    val query: String = "",
) : Parcelable
