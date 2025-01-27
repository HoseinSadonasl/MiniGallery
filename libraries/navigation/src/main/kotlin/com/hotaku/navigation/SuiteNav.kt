package com.hotaku.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.home.navigation.HomeScreenRoute
import com.hotaku.home.navigation.HomeScreenRoute.homeScreen

@Composable
fun SuiteNav(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    var selectedDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val layoutType =
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
                navHostController.navigate(HomeScreenRoute)
            }

            AppDestinations.ALBUMS -> {
            }
        }
    }

    NavigationSuiteScaffold(
        layoutType = layoutType,
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
    ) {
        NavHost(
            navController = navHostController,
            startDestination = HomeScreenRoute,
        ) {
            homeScreen(
                modifier = modifier,
                onShowSnackBar = { },
            )
        }
    }
}
