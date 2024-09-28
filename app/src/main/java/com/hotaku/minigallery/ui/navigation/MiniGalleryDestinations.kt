package com.hotaku.minigallery.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.hotaku.minigallery.R

enum class MiniGalleryDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
) {
    HOME(R.string.mini_gallery_destinations_home, Icons.Default.Home),
    ALBUMS(R.string.mini_gallery_destinations_all_media, Icons.AutoMirrored.Default.List),
    SETTINGS(R.string.mini_gallery_destinations_settings, Icons.Default.Settings),
}
