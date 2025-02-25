package com.hotaku.media.screens.albums.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.navigation.sharedHiltViewModel
import com.hotaku.media.screens.albums.AlbumsScreen
import com.hotaku.media.screens.albums.AlbumsViewModel
import com.hotaku.media.screens.media_detail.navigation.navigateToMediaDetailScreen
import com.hotaku.media.screens.shared.SharedMediaViewModel
import kotlinx.serialization.Serializable

@Serializable
object AlbumsScreenRoute

internal fun NavGraphBuilder.albumsNav(navHostController: NavHostController) =
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
                navHostController.navigateToMediaDetailScreen(initialIndex = albumsUiState.selectedMediaIndex) {
                    launchSingleTop = true
                }
            },
        )
    }

internal fun NavHostController.navigateToAlbumsScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(AlbumsScreenRoute) {
        navOptions()
    }
}
