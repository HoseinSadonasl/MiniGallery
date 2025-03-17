package com.hotaku.media_details

internal interface MediaDetailScreenEvents {
    data object OnShowSnackBar : MediaDetailScreenEvents

    data object OnShareMedia : MediaDetailScreenEvents

    data object OnPlayVideo : MediaDetailScreenEvents
}
