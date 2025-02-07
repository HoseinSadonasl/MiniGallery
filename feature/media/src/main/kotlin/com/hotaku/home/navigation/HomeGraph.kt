package com.hotaku.home.navigation

import android.os.Parcelable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hotaku.home.HomeScreen
import com.hotaku.home.PermissionsScreen
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

// Home routes
@Serializable
@Parcelize
object HomeScreenRoute : Parcelable

@Serializable
@Parcelize
internal object PermissionsScreenRoute : Parcelable

@Serializable
@Parcelize
object MediaPreviewScreen : Parcelable

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
