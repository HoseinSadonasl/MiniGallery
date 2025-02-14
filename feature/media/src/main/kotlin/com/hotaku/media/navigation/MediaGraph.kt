package com.hotaku.media.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.hotaku.media.screens.albums.AlbumsScreen
import com.hotaku.media.screens.albums.AlbumsScreenActions
import com.hotaku.media.screens.albums.AlbumsViewModel
import com.hotaku.media.screens.media.MediaScreen
import com.hotaku.media.screens.media.MediaScreenActions
import com.hotaku.media.screens.media.MediaViewModel
import com.hotaku.media.screens.permissions.PermissionsScreen
import kotlinx.serialization.Serializable

// Home routes
@Serializable
object MediaScreenRRoute

@Serializable
object AlbumsScreenRoute

@Serializable
internal object PermissionsScreenRoute

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

                val mediaScreenUiState by mediaViewModel.mediaScreenUiState.collectAsStateWithLifecycle()
                val mediaState = mediaViewModel.mediaUiState.collectAsLazyPagingItems()

                val albumsViewModel: AlbumsViewModel = hiltViewModel()
                val albumsUiState by albumsViewModel.albumsState.collectAsStateWithLifecycle()

                LaunchedEffect(mediaScreenUiState.selectedAlbum) {
                    mediaViewModel.onAction(
                        MediaScreenActions.OnUpdateMedia,
                    )
                }

                LaunchedEffect(albumsUiState.selectedAlbum) {
                    mediaViewModel.onAction(
                        MediaScreenActions.OnAlbumSelected(
                            album = albumsUiState.selectedAlbum,
                        ),
                    )
                }

                LaunchedEffect(mediaState) {
                    albumsViewModel.onAction(
                        AlbumsScreenActions.OnOpenAlbum(mediaState),
                    )
                }

                AlbumsScreen(
                    albumsViewModel = albumsViewModel,
                )
            }
        }
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedHiltViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel<T>()
    val parentEntry =
        remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
    return hiltViewModel(
        viewModelStoreOwner = parentEntry,
    )
}
