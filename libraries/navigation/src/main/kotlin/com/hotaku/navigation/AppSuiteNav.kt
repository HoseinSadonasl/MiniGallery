package com.hotaku.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.home.navigation.HomeScreenGraph
import com.hotaku.home.navigation.HomeScreenGraph.homeScreenGraph

@Composable
fun AppSuiteNav(
    navHostController: NavHostController,
) {
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
                navHostController.navigate(HomeScreenGraph)
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
            modifier = Modifier.statusBarsPadding(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navHostController,
                startDestination = HomeScreenGraph,
            ) {
                homeScreenGraph(
                    onShowSnackBar = { message ->
                        snackbarHostState.showSnackbar(
                            message = message,
                        )
                    },
                )
            }
        }
    }
}
