package com.hotaku.navigation.nav_routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.MediaListScreen
import com.hotaku.media.MediaListScreenRRoute
import com.hotaku.onboarding.OnboardingRoute

fun NavGraphBuilder.mediaListNav(navHostController: NavHostController) =
    composable<MediaListScreenRRoute> {
        MediaListScreen(
            navigateToMediaDetailScreen = { selectedMediaIndex ->
                navHostController.navigateToMediaDetailScreen(initialIndex = selectedMediaIndex) {
                    launchSingleTop = true
                }
            },
            navigateToOnboardingScreen = {
                navHostController.navigateToOnboardingScreen {
                    popUpTo<OnboardingRoute> {
                        inclusive = true
                    }
                }
            },
        )
    }

fun NavHostController.navigateToMediaListScreen(navOptions: NavOptionsBuilder.() -> Unit) {
    navigate(MediaListScreenRRoute) {
        navOptions()
    }
}
