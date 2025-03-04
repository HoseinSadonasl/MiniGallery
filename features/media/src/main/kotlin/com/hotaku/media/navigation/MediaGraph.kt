package com.hotaku.media.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.hotaku.media.screens.albums.navigation.albumsNav
import com.hotaku.media.screens.media_detail.navigation.mediaDetailsNav
import com.hotaku.media.screens.media_list.navigation.MediaListScreenRRoute
import com.hotaku.media.screens.media_list.navigation.mediaListNav
import com.hotaku.media.screens.onboarding.navigation.onboardingNav
import kotlinx.serialization.Serializable

@Serializable
object MediaGraph {
    fun NavGraphBuilder.mediaGraph(navHostController: NavHostController) {
        navigation<MediaGraph>(
            startDestination = MediaListScreenRRoute,
        ) {
            onboardingNav(
                navHostController = navHostController,
            )
            mediaListNav(navHostController = navHostController)
            albumsNav(navHostController = navHostController)
            mediaDetailsNav(navHostController = navHostController)
        }
    }
}
