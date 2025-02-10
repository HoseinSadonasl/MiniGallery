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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
internal fun MiniGalleryNavigationSuite(
    layoutType: NavigationSuiteType,
    navBackStackEntry: NavBackStackEntry?,
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
                    selected = currentDestination.isRouteInHierarchy(route = route.route),
                    onNavItemClick = onRouteSelected,
                )
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: Any): Boolean {
    if (this == null) return false
    return hierarchy?.any { it.hasRoute(route::class) } ?: false
}

private fun NavigationSuiteScope.navigationSuitItem(
    topLevelRoute: TopLevelRoute,
    selected: Boolean,
    onNavItemClick: (Any) -> Unit,
) {
    item(
        selected = selected,
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
