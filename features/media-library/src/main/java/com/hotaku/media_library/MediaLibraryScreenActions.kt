package com.hotaku.media_library

internal sealed interface MediaLibraryScreenActions {
    data object OnUpdateMediaState : MediaLibraryScreenActions

    data object OnClearMedia : MediaLibraryScreenActions

    data object OnFavoriteFolderClick : MediaLibraryScreenActions

    data object OnTrashFolderClick : MediaLibraryScreenActions

    data object OnSecureFolderClick : MediaLibraryScreenActions

    data object OnCloseSelectedFolder : MediaLibraryScreenActions
}
