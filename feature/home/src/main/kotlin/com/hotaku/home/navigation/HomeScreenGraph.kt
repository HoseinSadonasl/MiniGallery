package com.hotaku.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hotaku.home.HomeScreen
import kotlinx.serialization.Serializable

// Home routes
@Serializable
internal object HomeScreenRoute

@Serializable
object HomeScreenGraph {
    fun NavGraphBuilder.homeScreenGraph(
        onShowSnackBar: suspend (String) -> Unit,
    ) {
        navigation<HomeScreenGraph>(
            startDestination = HomeScreenRoute,
        ) {
            composable<HomeScreenRoute> {
                HomeScreen(
                    onShowSnackBar = { onShowSnackBar(it) },
                )
            }
        }
    }
}
