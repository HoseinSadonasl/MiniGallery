package com.hotaku.media.screens.onboarding.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.screens.media_list.navigation.MediaListScreenRRoute
import com.hotaku.media.screens.media_list.navigation.navigateToMediaListScreen
import com.hotaku.media.screens.onboarding.OnboardingScreen
import com.hotaku.media.screens.onboarding.OnboardingViewModel
import kotlinx.serialization.Serializable

@Serializable
internal object OnboardingRoute

internal fun NavGraphBuilder.onboardingNav(navHostController: NavHostController) =
    composable<OnboardingRoute>(
        popExitTransition = { slideOutOfContainer(SlideDirection.Right) },
    ) {
        val onboardingViewModel = hiltViewModel<OnboardingViewModel>()

        OnboardingScreen(
            onboardingViewModel = onboardingViewModel,
            navigateToMediaListScreen = {
                navHostController.navigateToMediaListScreen {
                    popUpTo<MediaListScreenRRoute> {
                        inclusive = true
                    }
                }
            },
        )
    }

internal fun NavHostController.navigateToOnboardingScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = OnboardingRoute) {
        navOptions()
    }
}
