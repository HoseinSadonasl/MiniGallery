package com.hotaku.albums.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.albums.AlbumsScreen
import com.hotaku.albums.AlbumsViewModel
import kotlinx.serialization.Serializable

@Serializable
object AlbumsScreenRoute

fun NavGraphBuilder.albumsNav(navigateToMediaDetailScreen: (Int?, String) -> Unit) =
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
            navigateToMediaDetailScreen = navigateToMediaDetailScreen,
        )
    }

fun NavHostController.navigateToAlbumsScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(AlbumsScreenRoute) {
        navOptions()
    }
}
