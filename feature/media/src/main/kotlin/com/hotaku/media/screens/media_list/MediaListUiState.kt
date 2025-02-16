package com.hotaku.media.screens.media_list

import android.os.Parcelable
import com.hotaku.media.model.AlbumUi
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaListUiState(
    val isSearchExpanded: Boolean = false,
    val showSyncSection: Boolean = true,
    val isTopBarVisible: Boolean = true,
    val selectedAlbum: AlbumUi? = null,
    val selectedMediaIndex: Int? = null,
    val mimeType: String = "",
    val query: String = "",
) : Parcelable
