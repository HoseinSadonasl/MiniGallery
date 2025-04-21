package com.hotaku.media_library

import com.hotaku.media_library.utils.LibraryFolderItem
import com.hotaku.ui.models.MediaUi

internal sealed interface MediaLibraryScreenActions {
    data object OnUpdateMediaState : MediaLibraryScreenActions

    data object OnClearMedia : MediaLibraryScreenActions

    data class OnFolderClick(val folderItem: LibraryFolderItem) : MediaLibraryScreenActions

    data class OnEmptyTrashClick(val media: List<MediaUi>) : MediaLibraryScreenActions
}
