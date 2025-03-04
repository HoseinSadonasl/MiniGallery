package com.hotaku.media.screens.albums.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.screens.albums.AlbumsScreen
import com.hotaku.media.screens.albums.AlbumsViewModel
import com.hotaku.media.screens.media_detail.navigation.navigateToMediaDetailScreen
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
        val albumsViewModel = hiltViewModel<AlbumsViewModel>()

        AlbumsScreen(
            albumsViewModel = albumsViewModel,
            navigateToMediaDetailScreen = { selectedMediaIndex, selectedAlbum ->
                navHostController.navigateToMediaDetailScreen(
                    initialIndex = selectedMediaIndex,
                    selectedAlbum = selectedAlbum,
                ) {
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
