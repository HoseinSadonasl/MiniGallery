package com.hotaku.media.screens.media_list.navigation

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
import com.hotaku.media.screens.media_detail.navigation.navigateToMediaDetailScreen
import com.hotaku.media.screens.media_list.MediaListScreen
import com.hotaku.media.screens.media_list.MediaListViewModel
import com.hotaku.media.screens.onboarding.navigation.OnboardingRoute
import com.hotaku.media.screens.onboarding.navigation.navigateToOnboardingScreen
import com.hotaku.media.screens.shared.SharedMediaViewModel
import kotlinx.serialization.Serializable

@Serializable
object MediaListScreenRRoute

internal fun NavGraphBuilder.mediaListNav(navHostController: NavHostController) =
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
                navHostController.navigateToMediaDetailScreen(initialIndex = mediaListScreenState.selectedMediaIndex) {
                    launchSingleTop = true
                }
            },
            navigateTounboardingScreen = {
                navHostController.navigateToOnboardingScreen {
                    popUpTo<OnboardingRoute> {
                        inclusive = true
                    }
                }
            },
        )
    }

internal fun NavHostController.navigateToMediaListScreen(navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(MediaListScreenRRoute) {
        navOptions()
    }
}
