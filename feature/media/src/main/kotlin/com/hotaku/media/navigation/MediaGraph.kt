package com.hotaku.media.navigation

import android.os.Parcelable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hotaku.media.screens.media.MediaScreen
import com.hotaku.media.screens.permissions.PermissionsScreen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

// Home routes
@Serializable
@Parcelize
object MediaScreenRRoute : Parcelable

@Serializable
@Parcelize
object AlbumsScreen : Parcelable

@Serializable
@Parcelize
internal object PermissionsScreenRoute : Parcelable

@Serializable
object MediaGraph {
    fun NavGraphBuilder.mediaGraph(
        navHostController: NavHostController,
        onShowSnackBar: suspend (String) -> Unit,
        permissionState: Boolean,
        onRequestPermissions: () -> Unit,
    ) {
        navigation<MediaGraph>(
            startDestination = if (permissionState) MediaScreenRRoute else PermissionsScreenRoute,
        ) {
            composable<PermissionsScreenRoute> {
                PermissionsScreen(
                    permissionState = permissionState,
                    onRequestPermissions = onRequestPermissions,
                    navigateToMediaScreen = { navHostController.navigate(MediaScreenRRoute) },
                )
            }
            composable<MediaScreenRRoute> {
                MediaScreen(
                    onShowSnackBar = { onShowSnackBar(it) },
                )
            }
        }
    }
}
