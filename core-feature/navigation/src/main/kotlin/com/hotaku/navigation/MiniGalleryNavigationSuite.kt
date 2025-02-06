package com.hotaku.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
internal fun MiniGalleryNavigationSuite(
    layoutType: NavigationSuiteType,
    navBackStackEntry: NavBackStackEntry?,
    selectedRoute: Any,
    onRouteSelected: (Any) -> Unit,
) {
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar =
        topLevelAppTopLevelRoutes.any {
            currentDestination?.hasRoute(it.route::class) == true
        }

    if (showBottomBar) {
        NavigationSuite(
            layoutType = layoutType,
        ) {
            topLevelAppTopLevelRoutes.forEach { route ->
                navigationSuitItem(
                    topLevelRoute = route,
                    selectedRoute = selectedRoute,
                    onNavItemClick = onRouteSelected,
                )
            }
        }
    }
}

private fun NavigationSuiteScope.navigationSuitItem(
    topLevelRoute: TopLevelRoute,
    selectedRoute: Any,
    onNavItemClick: (Any) -> Unit,
) {
    item(
        selected = selectedRoute == topLevelRoute.route,
        onClick = { onNavItemClick(topLevelRoute.route) },
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = topLevelRoute.icon),
                contentDescription = stringResource(id = topLevelRoute.label),
            )
        },
        label = {
            Text(text = stringResource(id = topLevelRoute.label))
        },
    )
}
