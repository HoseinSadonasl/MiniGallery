package com.hotaku.onboarding.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.onboarding.OnboardingScreen
import com.hotaku.onboarding.OnboardingViewModel
import kotlinx.serialization.Serializable

@Serializable
object OnboardingRoute

fun NavGraphBuilder.onboardingNav(navigateToMediaListScreen: () -> Unit) =
    composable<OnboardingRoute>(
        popExitTransition = { slideOutOfContainer(SlideDirection.Right) },
    ) {
        val onboardingViewModel = hiltViewModel<OnboardingViewModel>()

        OnboardingScreen(
            onboardingViewModel = onboardingViewModel,
            navigateToMediaListScreen = {
//                navHostController.navigateToMediaListScreen {
//                    popUpTo<MediaListScreenRRoute> {
//                        inclusive = true
//                    }
//                }
            },
        )
    }

fun NavHostController.navigateToOnboardingScreen(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = OnboardingRoute) {
        navOptions()
    }
}
