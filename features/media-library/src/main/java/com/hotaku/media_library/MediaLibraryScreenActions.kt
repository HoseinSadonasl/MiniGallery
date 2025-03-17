package com.hotaku.media_library

import com.hotaku.media_library.utils.LibraryFolderItem

internal sealed interface MediaLibraryScreenActions {
    data object OnUpdateMediaState : MediaLibraryScreenActions

    data object OnClearMedia : MediaLibraryScreenActions

    data class OnFolderClick(val folderItem: LibraryFolderItem) : MediaLibraryScreenActions
}
