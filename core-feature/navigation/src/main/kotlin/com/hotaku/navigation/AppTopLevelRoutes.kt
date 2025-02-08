package com.hotaku.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hotaku.core_feature.navigation.R
import com.hotaku.media.navigation.AlbumsScreenRoute
import com.hotaku.media.navigation.MediaScreenRRoute

internal data class TopLevelRoute(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: Any,
)

internal val topLevelAppTopLevelRoutes
    get() =
        listOf(
            TopLevelRoute(
                label = R.string.navigation_route_title_all_media,
                icon = R.drawable.navigation_icon_image,
                route = MediaScreenRRoute,
            ),
            TopLevelRoute(
                label = R.string.navigation_route_title_albums,
                icon = R.drawable.navigation_icon_albums,
                route = AlbumsScreenRoute,
            ),
        )
