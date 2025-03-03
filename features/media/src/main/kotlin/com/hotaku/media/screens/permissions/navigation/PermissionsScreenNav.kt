package com.hotaku.media.screens.permissions.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hotaku.media.screens.media_list.navigation.MediaListScreenRRoute
import com.hotaku.media.screens.media_list.navigation.navigateToMediaListScreen
import com.hotaku.media.screens.permissions.PermissionsScreen
import kotlinx.serialization.Serializable

@Serializable internal object PermissionsScreenRoute

internal fun NavGraphBuilder.permissionsNav(
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
    navHostController: NavHostController,
) = composable<PermissionsScreenRoute>(
    popExitTransition = { slideOutOfContainer(SlideDirection.Right) },
) {
    PermissionsScreen(
        permissionState = permissionState,
        onRequestPermissions = onRequestPermissions,
        navigateToMediaScreen = {
            navHostController.navigateToMediaListScreen {
                popUpTo<MediaListScreenRRoute> {
                    inclusive = true
                }
            }
        },
    )
}
