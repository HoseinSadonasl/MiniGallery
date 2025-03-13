package com.hotaku.navigation.nav_routes

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hotaku.albums.AlbumsScreen
import com.hotaku.albums.AlbumsScreenRoute

fun NavGraphBuilder.albumsNav(navHostController: NavHostController) =
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
        AlbumsScreen(
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
