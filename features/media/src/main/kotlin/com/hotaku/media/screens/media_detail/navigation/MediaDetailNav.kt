package com.hotaku.media.screens.media_detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.hotaku.media.navigation.sharedHiltViewModel
import com.hotaku.media.screens.media_detail.MediaDetailScreen
import com.hotaku.media.screens.media_detail.MediaDetailViewModel
import com.hotaku.media.screens.shared.SharedMediaViewModel
import kotlinx.serialization.Serializable

@Serializable
internal data class MediaDetailRoute(
    val initialItemIndex: Int?,
)

internal fun NavGraphBuilder.mediaDetailsNav(navHostController: NavHostController) =
    composable<MediaDetailRoute> { navBackStackEntry ->
        val initialIndex = navBackStackEntry.toRoute<MediaDetailRoute>().initialItemIndex ?: 0

        val sharedMediaViewModel =
            navBackStackEntry.sharedHiltViewModel<SharedMediaViewModel>(
                navController = navHostController,
            )

        val mediaDetailViewModel = hiltViewModel<MediaDetailViewModel>()

        val sharedMediaState by sharedMediaViewModel.mediaUiState.collectAsStateWithLifecycle()

        LaunchedEffect(sharedMediaState) {
            mediaDetailViewModel.setMediaState(
                mediaState = sharedMediaState,
            )
        }

        MediaDetailScreen(
            mediaDetailViewModel = mediaDetailViewModel,
            selectedMediaItemIndex = initialIndex,
            navigateUp = { navHostController.navigateUp() },
        )
    }

internal fun NavHostController.navigateToMediaDetailScreen(
    initialIndex: Int?,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(
        MediaDetailRoute(
            initialItemIndex = initialIndex,
        ),
    ) {
        navOptions()
    }
}
