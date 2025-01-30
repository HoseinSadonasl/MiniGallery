package com.hotaku.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hotaku.home.HomeScreen
import com.hotaku.home.PermissionsScreen
import kotlinx.serialization.Serializable

// Home routes
@Serializable
internal object PermissionsScreenRoute

@Serializable
internal object HomeScreenRoute

@Serializable
object HomeGraph {
    fun NavGraphBuilder.homeScreenGraph(
        navHostController: NavHostController,
        onShowSnackBar: suspend (String) -> Unit,
        permissionState: Boolean,
        onRequestPermissions: () -> Unit,
    ) {
        navigation<HomeGraph>(
            startDestination = if (permissionState) HomeScreenRoute else PermissionsScreenRoute,
        ) {
            composable<PermissionsScreenRoute> {
                PermissionsScreen(
                    permissionState = permissionState,
                    onRequestPermissions = onRequestPermissions,
                    navigateToHomeScreen = { navHostController.navigate(HomeScreenRoute) },
                )
            }
            composable<HomeScreenRoute> {
                HomeScreen(
                    onShowSnackBar = { onShowSnackBar(it) },
                )
            }
        }
    }
}
