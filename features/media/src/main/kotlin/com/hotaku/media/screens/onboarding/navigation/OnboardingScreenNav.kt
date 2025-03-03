package com.hotaku.media.screens.onboarding.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hotaku.media.screens.media_list.navigation.MediaListScreenRRoute
import com.hotaku.media.screens.media_list.navigation.navigateToMediaListScreen
import com.hotaku.media.screens.onboarding.OnboardingScreen
import kotlinx.serialization.Serializable

@Serializable internal object OnboardingRoute

internal fun NavGraphBuilder.onboardingNav(
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
    navHostController: NavHostController,
) = composable<OnboardingRoute>(
    popExitTransition = { slideOutOfContainer(SlideDirection.Right) },
) {
    OnboardingScreen(
        permissionState = permissionState,
        onRequestPermissions = onRequestPermissions,
        navigateToMediaScreen = {
            navHostController.navigateToMediaListScreen {
                popUpTo<MediaListScreenRRoute> {
                    inclusive = true
                }
            }
        },
    )
}
