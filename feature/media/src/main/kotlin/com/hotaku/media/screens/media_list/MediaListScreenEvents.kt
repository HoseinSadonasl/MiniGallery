package com.hotaku.media.screens.media_list

interface MediaListScreenEvents {
    data object OnRefreshList : MediaListScreenEvents

    data object OnCloseMediaListPreview : MediaListScreenEvents

    object OnShareMediaList : MediaListScreenEvents

    data object OnNavigateToMediaDetail : MediaListScreenEvents
}
