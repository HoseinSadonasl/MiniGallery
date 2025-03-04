package com.hotaku.media.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.MediaListScreen
import com.hotaku.media.MediaListViewModel
import kotlinx.serialization.Serializable

@Serializable
object MediaListScreenRRoute

fun NavGraphBuilder.mediaListNav(
    navigateToOnboardingScreen: () -> Unit,
    navigateToMediaDetailScreen: (Int?) -> Unit,
) = composable<MediaListScreenRRoute>(
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
    MediaListScreen(
        mediaListViewModel = hiltViewModel<MediaListViewModel>(),
        navigateToMediaDetailScreen = navigateToMediaDetailScreen,
        navigateToOnboardingScreen = navigateToOnboardingScreen,
    )
}

fun NavHostController.navigateToMediaListScreen(navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(MediaListScreenRRoute) {
        navOptions()
    }
}
