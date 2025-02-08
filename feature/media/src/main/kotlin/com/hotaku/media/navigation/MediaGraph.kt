package com.hotaku.media.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hotaku.media.screens.albums.AlbumsScreen
import com.hotaku.media.screens.albums.AlbumsViewModel
import com.hotaku.media.screens.media.MediaScreen
import com.hotaku.media.screens.media.MediaScreenActions
import com.hotaku.media.screens.media.MediaViewModel
import com.hotaku.media.screens.permissions.PermissionsScreen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

// Home routes
@Serializable
@Parcelize
object MediaScreenRRoute : Parcelable

@Serializable
@Parcelize
object AlbumsScreenRoute : Parcelable

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
            composable<MediaScreenRRoute> { navBackStackEntry ->
                val mediaViewModel =
                    navBackStackEntry.sharedHiltViewModel<MediaViewModel>(
                        navController = navHostController,
                    )

                MediaScreen(
                    mediaViewModel = mediaViewModel,
                    onShowSnackBar = { onShowSnackBar(it) },
                )
            }
            composable<AlbumsScreenRoute> { navBackStackEntry ->
                val mediaViewModel =
                    navBackStackEntry.sharedHiltViewModel<MediaViewModel>(
                        navController = navHostController,
                    )

                val albumsViewModel: AlbumsViewModel = hiltViewModel()

                AlbumsScreen(
                    albumsViewModel = albumsViewModel,
                    onNavigateWithAlbum = { album ->
                        mediaViewModel.onAction(
                            MediaScreenActions.OnAlbumSelected(
                                album = album,
                            ),
                        )
                        navHostController.popBackStack()
                    },
                )
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedHiltViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel<T>()
    val parentEntry =
        remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
    return hiltViewModel(
        viewModelStoreOwner = parentEntry,
    )
}
