package com.hotaku.media.screens.media_detail

internal interface MediaDetailScreenEvents {
    data object OnViewMedia : MediaDetailScreenEvents

    data object OnShareMedia : MediaDetailScreenEvents

    data object OnRefreshMedia : MediaDetailScreenEvents
}
