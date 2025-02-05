package com.hotaku.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hotaku.core_feature.navigation.R

enum class AppDestinations(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
) {
    HOME(R.string.navigation_route_title_all_media, R.drawable.navigation_icon_image),
    ALBUMS(R.string.navigation_route_title_albums, R.drawable.navigation_icon_albums),
}
