package com.hotaku.media.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.hotaku.media.screens.albums.navigation.albumsNav
import com.hotaku.media.screens.media_detail.navigation.mediaDetailsNav
import com.hotaku.media.screens.media_list.navigation.MediaListScreenRRoute
import com.hotaku.media.screens.media_list.navigation.mediaListNav
import com.hotaku.media.screens.permissions.navigation.PermissionsScreenRoute
import com.hotaku.media.screens.permissions.navigation.permissionsNav
import kotlinx.serialization.Serializable

@Serializable
object MediaGraph {
    fun NavGraphBuilder.mediaGraph(
        navHostController: NavHostController,
        onShowSnackBar: suspend (String) -> Unit,
        permissionState: Boolean,
        onRequestPermissions: () -> Unit,
    ) {
        navigation<MediaGraph>(
            startDestination = if (permissionState) MediaListScreenRRoute else PermissionsScreenRoute,
        ) {
            permissionsNav(
                permissionState = permissionState,
                onRequestPermissions = onRequestPermissions,
                navHostController = navHostController,
            )
            mediaListNav(navHostController = navHostController)
            albumsNav(navHostController = navHostController)
            mediaDetailsNav(navHostController = navHostController)
        }
    }
}
