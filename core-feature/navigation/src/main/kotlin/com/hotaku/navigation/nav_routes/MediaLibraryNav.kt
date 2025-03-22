package com.hotaku.navigation.nav_routes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hotaku.media_library.MediaLibraryRoute
import com.hotaku.media_library.MediaLibraryScreen
import com.hotaku.media_library.utils.LibraryFolderType.FAVORITE_FOLDER
import com.hotaku.media_library.utils.LibraryFolderType.TRASH_FOLDER

fun NavGraphBuilder.mediaLibraryNav(navHostController: NavHostController) =
    composable<MediaLibraryRoute> {
        MediaLibraryScreen(
            navigateToMediaDetailScreen = { index, folderType ->
                navHostController.navigateToMediaDetailScreen(
                    initialIndex = index,
                    matchTrash = folderType == TRASH_FOLDER,
                    matchFavorite = folderType == FAVORITE_FOLDER,
                ) {
                    launchSingleTop = true
                }
            },
        )
    }
