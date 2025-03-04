package com.hotaku.media.screens.media_detail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media.screens.media_detail.MediaDetailScreen
import com.hotaku.media.screens.media_detail.MediaDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
internal data class MediaDetailRoute(
    val initialItemIndex: Int?,
    val selectedAlbum: String? = null,
)

internal fun NavGraphBuilder.mediaDetailsNav(navHostController: NavHostController) =
    composable<MediaDetailRoute> {
        val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()

        MediaDetailScreen(
            mediaDetailViewModel = mediaDetailViewModel,
            navigateUp = { navHostController.navigateUp() },
        )
    }

internal fun NavHostController.navigateToMediaDetailScreen(
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
