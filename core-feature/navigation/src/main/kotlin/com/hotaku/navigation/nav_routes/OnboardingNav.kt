package com.hotaku.navigation.nav_routes

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.MediaListScreenRRoute
import com.hotaku.onboarding.OnboardingRoute
import com.hotaku.onboarding.OnboardingScreen

fun NavGraphBuilder.onboardingNav(navHostController: NavHostController) =
    composable<OnboardingRoute>(
        popExitTransition = { slideOutOfContainer(SlideDirection.Right) },
    ) {
        OnboardingScreen(
            navigateToMediaListScreen = {
                navHostController.navigateToMediaListScreen {
                    popUpTo<MediaListScreenRRoute> {
                        inclusive = true
                    }
                }
            },
        )
    }

fun NavHostController.navigateToOnboardingScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = OnboardingRoute) {
        navOptions()
    }
}
