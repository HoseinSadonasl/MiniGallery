package com.hotaku.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.albums.navigation.albumsNav
import com.hotaku.media.navigation.MediaListScreenRRoute
import com.hotaku.media.navigation.mediaListNav
import com.hotaku.media.navigation.navigateToMediaListScreen
import com.hotaku.media_details.navigation.mediaDetailsNav
import com.hotaku.media_details.navigation.navigateToMediaDetailScreen
import com.hotaku.media_library.navigation.mediaLibraryNav
import com.hotaku.onboarding.navigation.OnboardingRoute
import com.hotaku.onboarding.navigation.navigateToOnboardingScreen
import com.hotaku.onboarding.navigation.onboardingNav

@Composable
fun AppSuiteNav(navHostController: NavHostController) {
    val direction: LayoutDirection = LocalLayoutDirection.current
    val snackbarHostState: SnackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    val navBackStackEntry: NavBackStackEntry? by navHostController.currentBackStackEntryAsState()

    val windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
    val layoutType: NavigationSuiteType =
        when (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> {
                NavigationSuiteType.NavigationBar
            }

            else -> {
                NavigationSuiteType.NavigationRail
            }
        }

    NavigationSuiteScaffoldLayout(
        navigationSuite = {
            MiniGalleryNavigationSuite(
                layoutType = layoutType,
                navBackStackEntry = navBackStackEntry,
                onRouteSelected = { route ->
                    navHostController.navigate(route = route, navOptions = topLevelNavOptions)
                },
            )
        },
        layoutType = layoutType,
        content = {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            ) { paddingValues ->
                MiniGalleryNavHost(
                    modifier =
                        Modifier.consumeWindowInsets(
                            paddingValues =
                                PaddingValues(
                                    start = paddingValues.calculateLeftPadding(direction),
                                    top = 0.dp,
                                    end = paddingValues.calculateRightPadding(direction),
                                    bottom = 0.dp,
                                ),
                        ),
                    navHostController = navHostController,
                )
            }
        },
    )
}

private val topLevelNavOptions: NavOptions
    get() =
        navOptions {
            popUpTo(MediaListScreenRRoute) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }

@Composable
private fun MiniGalleryNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = MediaListScreenRRoute,
    ) {
        onboardingNav(
            navigateToMediaListScreen = {
                navHostController.navigateToMediaListScreen {
                    popUpTo<MediaListScreenRRoute> {
                        inclusive = true
                    }
                }
            },
        )

        mediaListNav(
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

        albumsNav(
            navigateToMediaDetailScreen = { selectedMediaIndex, selectedAlbum ->
                navHostController.navigateToMediaDetailScreen(
                    initialIndex = selectedMediaIndex,
                    selectedAlbum = selectedAlbum,
                ) {
                    launchSingleTop = true
                }
            },
        )

        mediaLibraryNav()

        mediaDetailsNav(
            navigateUp = {
                navHostController.popBackStack()
            },
        )
    }
}
