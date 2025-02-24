package com.hotaku.media.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.hotaku.media.screens.albums.AlbumsScreen
import com.hotaku.media.screens.albums.AlbumsViewModel
import com.hotaku.media.screens.media_detail.MediaDetailScreen
import com.hotaku.media.screens.media_detail.MediaDetailViewModel
import com.hotaku.media.screens.media_list.MediaListScreen
import com.hotaku.media.screens.media_list.MediaListViewModel
import com.hotaku.media.screens.permissions.PermissionsScreen
import com.hotaku.media.screens.shared.SharedMediaViewModel
import kotlinx.serialization.Serializable

// Home routes
@Serializable
object MediaListScreenRRoute

@Serializable
object AlbumsScreenRoute

@Serializable
internal data class MediaDetailRoute(
    val initialItemIndex: Int?,
)

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
            startDestination = if (permissionState) MediaListScreenRRoute else PermissionsScreenRoute,
        ) {
            composable<PermissionsScreenRoute>(
                popExitTransition = { slideOutOfContainer(SlideDirection.Right) },
            ) {
                PermissionsScreen(
                    permissionState = permissionState,
                    onRequestPermissions = onRequestPermissions,
                    navigateToMediaScreen = {
                        navHostController.navigate(MediaListScreenRRoute) {
                            popUpTo<MediaListScreenRRoute>()
                        }
                    },
                )
            }
            composable<MediaListScreenRRoute>(
                enterTransition = {
                    slideIntoContainer(
                        SlideDirection.Right,
                        animationSpec = tween(500),
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        SlideDirection.Left,
                        animationSpec = tween(500),
                    )
                },
            ) { navBackStackEntry ->
                val sharedMediaViewModel =
                    navBackStackEntry.sharedHiltViewModel<SharedMediaViewModel>(
                        navController = navHostController,
                    )

                val mediaListViewModel = hiltViewModel<MediaListViewModel>()

                val sharedMediaState by sharedMediaViewModel.mediaUiState.collectAsStateWithLifecycle()

                val mediaListScreenState by mediaListViewModel.mediaListScreenUiState.collectAsStateWithLifecycle()

                LaunchedEffect(sharedMediaState) {
                    mediaListViewModel.setMediaState(mediaState = sharedMediaState)
                }

                LaunchedEffect(
                    key1 = mediaListScreenState.mimeType,
                    key2 = mediaListScreenState.query,
                ) {
                    sharedMediaViewModel.updateMediaState(
                        mimeType = mediaListScreenState.mimeType,
                        query = mediaListScreenState.query,
                    )
                }

                MediaListScreen(
                    mediaListViewModel = mediaListViewModel,
                    navigateToMediaDetailScreen = {
                        navHostController.navigateToMediaDetailScreen(mediaListScreenState.selectedMediaIndex)
                    },
                    onShowSnackBar = { onShowSnackBar(it) },
                )
            }
            composable<AlbumsScreenRoute>(
                enterTransition = {
                    slideIntoContainer(
                        SlideDirection.Left,
                        animationSpec = tween(500),
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        SlideDirection.Right,
                        animationSpec = tween(500),
                    )
                },
            ) { navBackStackEntry ->
                val sharedMediaViewModel =
                    navBackStackEntry.sharedHiltViewModel<SharedMediaViewModel>(
                        navController = navHostController,
                    )

                val albumsViewModel = hiltViewModel<AlbumsViewModel>()

                val albumsUiState by albumsViewModel.albumsUiState.collectAsStateWithLifecycle()

                val sharedMediaState by sharedMediaViewModel.mediaUiState.collectAsStateWithLifecycle()

                LaunchedEffect(albumsUiState.selectedAlbum) {
                    albumsUiState.selectedAlbum?.displayName?.let { albumName ->
                        sharedMediaViewModel.updateMediaState(
                            albumName = albumName,
                        )
                    }
                }

                LaunchedEffect(sharedMediaState) {
                    albumsViewModel.setMediaState(mediaState = sharedMediaState)
                }

                AlbumsScreen(
                    albumsViewModel = albumsViewModel,
                    navigateToMediaDetailScreen = {
                        navHostController.navigateToMediaDetailScreen(albumsUiState.selectedMediaIndex)
                    },
                )
            }
            composable<MediaDetailRoute> { navBackStackEntry ->
                val initialIndex = navBackStackEntry.toRoute<MediaDetailRoute>().initialItemIndex ?: 0

                val sharedMediaViewModel =
                    navBackStackEntry.sharedHiltViewModel<SharedMediaViewModel>(
                        navController = navHostController,
                    )

                val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()

                val sharedMediaState by sharedMediaViewModel.mediaUiState.collectAsStateWithLifecycle()

                LaunchedEffect(sharedMediaState) {
                    mediaDetailViewModel.setMediaState(
                        mediaState = sharedMediaState,
                    )
                }

                MediaDetailScreen(
                    mediaDetailViewModel = mediaDetailViewModel,
                    selectedMediaItemIndex = initialIndex,
                    navigateUp = { navHostController.popBackStack() },
                )
            }
        }
    }
}

private fun NavHostController.navigateToMediaDetailScreen(selectedMediaIndex: Int?) {
    navigate(
        MediaDetailRoute(
            initialItemIndex = selectedMediaIndex,
        ),
    ) {
        launchSingleTop = true
    }
}
