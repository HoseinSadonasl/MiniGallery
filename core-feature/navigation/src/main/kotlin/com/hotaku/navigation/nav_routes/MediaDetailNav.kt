package com.hotaku.navigation.nav_routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media_details.MediaDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class MediaDetailRoute(
    val initialItemIndex: Int?,
    val selectedAlbum: String? = null,
)

fun NavGraphBuilder.mediaDetailsNav(navHostController: NavHostController) =
    composable<MediaDetailRoute> {
        MediaDetailScreen(
            navigateUp = {
                navHostController.popBackStack()
            },
        )
    }

fun NavHostController.navigateToMediaDetailScreen(
    initialIndex: Int?,
    selectedAlbum: String? = null,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        MediaDetailRoute(
            initialItemIndex = initialIndex,
            selectedAlbum = selectedAlbum,
        ),
    ) {
        navOptions()
    }
}
