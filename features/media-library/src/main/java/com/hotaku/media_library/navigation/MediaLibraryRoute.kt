package com.hotaku.media_library.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable object MediaLibraryRoute

fun NavGraphBuilder.mediaLibraryNav() =
    composable<MediaLibraryRoute>(
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
    ) {
    }

fun NavHostController.navigateToMediaListScreen(navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(MediaLibraryRoute) {
        navOptions()
    }
}
