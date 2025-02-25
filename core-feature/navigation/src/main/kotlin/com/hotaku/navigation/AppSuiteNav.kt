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
import com.hotaku.media.navigation.MediaGraph
import com.hotaku.media.navigation.MediaGraph.mediaGraph
import com.hotaku.media.screens.media_list.navigation.MediaListScreenRRoute

@Composable
fun AppSuiteNav(
    navHostController: NavHostController,
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
) {
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
                    snackbarHostState = snackbarHostState,
                    permissionState = permissionState,
                    onRequestPermissions = onRequestPermissions,
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
    snackbarHostState: SnackbarHostState,
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = MediaGraph,
    ) {
        mediaGraph(
            navHostController = navHostController,
            onShowSnackBar = { message ->
                snackbarHostState.showSnackbar(
                    message = message,
                )
            },
            permissionState = permissionState,
            onRequestPermissions = onRequestPermissions,
        )
    }
}
