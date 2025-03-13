package com.hotaku.navigation.nav_routes

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hotaku.media_library.MediaLibraryRoute

fun NavGraphBuilder.mediaLibraryNav(navHostController: NavHostController) =
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
