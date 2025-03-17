package com.hotaku.media_library

import androidx.paging.PagingData
import com.hotaku.media_library.utils.LibraryFolderType
import com.hotaku.ui.UiText
import com.hotaku.ui.models.MediaUi
import kotlinx.coroutines.flow.Flow

internal data class MediaLibraryUiState(
    val screenTitle: UiText? = null,
    val selectedFolder: LibraryFolderType? = null,
    val media: Flow<PagingData<MediaUi>>? = null,
    val mimeType: String = "",
    val query: String = "",
    val albumName: String = "",
)
