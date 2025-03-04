package com.hotaku.media_details

internal interface MediaDetailScreenEvents {
    data object OnViewMedia : MediaDetailScreenEvents

    data object OnShareMedia : MediaDetailScreenEvents

    data object OnRefreshMedia : MediaDetailScreenEvents
}
