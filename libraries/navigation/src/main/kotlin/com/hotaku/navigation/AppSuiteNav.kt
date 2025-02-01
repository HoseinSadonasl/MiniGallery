package com.hotaku.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.home.navigation.HomeGraph
import com.hotaku.home.navigation.HomeGraph.homeScreenGraph
import com.hotaku.home.navigation.HomeScreenRoute

@Composable
fun AppSuiteNav(
    navHostController: NavHostController,
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
) {
    val direction: LayoutDirection = LocalLayoutDirection.current
    val snackbarHostState: SnackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    var selectedDestination: AppDestinations by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

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

    LaunchedEffect(selectedDestination) {
        when (selectedDestination) {
            AppDestinations.HOME -> {
                if (selectedDestination != AppDestinations.HOME) {
                    navHostController.navigate(HomeScreenRoute)
                }
            }

            AppDestinations.ALBUMS -> {
            }
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    selected = selectedDestination == destination,
                    onClick = {
                        selectedDestination = destination
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(destination.icon),
                            contentDescription = destination.name,
                        )
                    },
                )
            }
        },
        layoutType = layoutType,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { paddingValues ->
            NavHost(
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
                navController = navHostController,
                startDestination = HomeGraph,
            ) {
                homeScreenGraph(
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
    }
}
