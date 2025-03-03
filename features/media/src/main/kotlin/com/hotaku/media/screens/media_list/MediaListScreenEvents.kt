package com.hotaku.media.screens.media_list

interface MediaListScreenEvents {
    object OnRefreshList : MediaListScreenEvents

    object OnCloseMediaListPreview : MediaListScreenEvents

    object OnShareMediaList : MediaListScreenEvents

    object OnNavigateToMediaDetail : MediaListScreenEvents
}
