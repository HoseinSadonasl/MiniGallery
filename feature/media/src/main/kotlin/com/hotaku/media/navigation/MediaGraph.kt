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
import com.hotaku.media.screens.media_detail.MediaDetailScreen
import com.hotaku.media.screens.media_detail.MediaDetailScreenActions
import com.hotaku.media.screens.media_detail.MediaDetailViewModel
import com.hotaku.media.screens.media_list.MediaListScreen
import com.hotaku.media.screens.media_list.MediaListScreenActions
import com.hotaku.media.screens.media_list.MediaViewModel
import com.hotaku.media.screens.permissions.PermissionsScreen
import kotlinx.serialization.Serializable

// Home routes
@Serializable
object MediaScreenRRoute

@Serializable
object AlbumsScreenRoute

@Serializable
object MediaDetailRoute

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

                MediaListScreen(
                    mediaViewModel = mediaViewModel,
                    navigateToMediaDetailScreen = {
                        navHostController.navigate(MediaDetailRoute)
                    },
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
                        MediaListScreenActions.OnUpdateMediaList,
                    )
                }

                LaunchedEffect(albumsUiState.selectedAlbum) {
                    mediaViewModel.onAction(
                        MediaListScreenActions.OnAlbumSelected(
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
            composable<MediaDetailRoute> { navBackStackEntry ->
                val mediaViewModel =
                    navBackStackEntry.sharedHiltViewModel<MediaViewModel>(
                        navController = navHostController,
                    )

                val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()

                val initialIndex = mediaViewModel.mediaScreenUiState.value.selectedMediaIndex

                val mediaState = mediaViewModel.mediaUiState.collectAsLazyPagingItems()
                LaunchedEffect(mediaState.loadState) {
                    if (mediaState.itemSnapshotList.isEmpty()) return@LaunchedEffect
                    mediaDetailViewModel.onAction(
                        MediaDetailScreenActions.OnAddmediaList(
                            media = mediaState.itemSnapshotList.items,
                            initialIndex = initialIndex ?: 0,
                        ),
                    )
                }

                MediaDetailScreen(
                    mediaDetailViewModel = mediaDetailViewModel,
                    navigateUp = { navHostController.popBackStack() },
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
