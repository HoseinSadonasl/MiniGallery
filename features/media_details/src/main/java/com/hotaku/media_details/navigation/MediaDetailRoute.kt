package com.hotaku.media_details.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.hotaku.media_details.MediaDetailScreen
import com.hotaku.media_details.MediaDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
data class MediaDetailRoute(
    val initialItemIndex: Int?,
    val selectedAlbum: String? = null,
)

fun NavGraphBuilder.mediaDetailsNav(navigateUp: () -> Unit) =
    composable<MediaDetailRoute> {
        val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()

        MediaDetailScreen(
            mediaDetailViewModel = mediaDetailViewModel,
            navigateUp = navigateUp,
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
