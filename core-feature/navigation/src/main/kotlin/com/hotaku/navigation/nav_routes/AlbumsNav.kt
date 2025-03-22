package com.hotaku.navigation.nav_routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hotaku.albums.AlbumsScreen
import com.hotaku.albums.AlbumsScreenRoute

fun NavGraphBuilder.albumsNav(navHostController: NavHostController) =
    composable<AlbumsScreenRoute> { navBackStackEntry ->
        AlbumsScreen(
            navigateToMediaDetailScreen = { selectedMediaIndex, selectedAlbum ->
                navHostController.navigateToMediaDetailScreen(
                    initialIndex = selectedMediaIndex,
                    selectedAlbum = selectedAlbum,
                ) {
                    launchSingleTop = true
                }
            },
        )
    }
