package com.hotaku.media

interface MediaListScreenEvents {
    object OnRefreshList : MediaListScreenEvents

    object OnCloseMediaListPreview : MediaListScreenEvents

    object OnShareMediaList : MediaListScreenEvents

    object OnNavigateToMediaDetail : MediaListScreenEvents
}
