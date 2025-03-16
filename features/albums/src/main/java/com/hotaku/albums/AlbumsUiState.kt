package com.hotaku.albums

import androidx.paging.PagingData
import com.hotaku.albums.model.AlbumUi
import com.hotaku.ui.UiState
import com.hotaku.ui.models.MediaUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal data class AlbumsUiState(
    val albums: UiState<List<AlbumUi>> = UiState.Success(emptyList()),
    val media: Flow<PagingData<MediaUi>> = emptyFlow(),
    val selectedAlbum: AlbumUi? = null,
    val selectedMediaIndex: Int? = null,
    val isSearchFocused: Boolean = false,
    val mimeType: String = "",
    val query: String = "",
)
