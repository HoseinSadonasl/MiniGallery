package com.hotaku.media

interface MediaListScreenEvents {
    object OnRefreshList : MediaListScreenEvents

    object NavigateToMediaDetail : MediaListScreenEvents

    object OnCloseMediaListPreview : MediaListScreenEvents

    object OnShareMediaList : MediaListScreenEvents

    object OnNavigateToMediaDetailScreen : MediaListScreenEvents

    object OnPlayVideo : MediaListScreenEvents
}
