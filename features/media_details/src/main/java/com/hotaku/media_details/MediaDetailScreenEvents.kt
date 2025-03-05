package com.hotaku.media_details

internal interface MediaDetailScreenEvents {
    data object OnRefreshMedia : MediaDetailScreenEvents

    data object OnShareMedia : MediaDetailScreenEvents

    data object OnPlayVideo : MediaDetailScreenEvents
}
