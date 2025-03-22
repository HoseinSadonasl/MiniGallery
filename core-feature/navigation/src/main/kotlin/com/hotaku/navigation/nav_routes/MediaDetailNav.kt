package com.hotaku.navigation.nav_routes

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media_details.MediaDetailRoute
import com.hotaku.media_details.MediaDetailScreen

fun NavGraphBuilder.mediaDetailsNav(navHostController: NavHostController) =
    composable<MediaDetailRoute>(
        enterTransition = {
            slideIntoContainer(
                SlideDirection.Up,
                animationSpec = tween(500),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                SlideDirection.Down,
                animationSpec = tween(500),
            )
        },
    ) {
        MediaDetailScreen(
            navigateUp = {
                navHostController.popBackStack()
            },
        )
    }

fun NavHostController.navigateToMediaDetailScreen(
    initialIndex: Int?,
    selectedAlbum: String? = null,
    matchTrash: Boolean = false,
    matchFavorite: Boolean = false,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        MediaDetailRoute(
            initialItemIndex = initialIndex,
            selectedAlbum = selectedAlbum,
            matchTrash = matchTrash,
            matchFavorite = matchFavorite,
        ),
    ) {
        navOptions()
    }
}
