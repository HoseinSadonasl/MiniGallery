package com.hotaku.media_library.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.hotaku.features.media_library.R
import com.hotaku.ui.UiText
import com.hotaku.ui.blue
import com.hotaku.ui.red
import com.hotaku.ui.yellow

enum class LibraryFolderType {
    FAVORITE_FOLDER,
    TRASH_FOLDER,
    SECURE_FOLDER,
}

internal data class LibraryFolderItem(
    val label: UiText,
    @DrawableRes val icon: Int,
    val color: Color,
    val type: LibraryFolderType,
)

internal val libraryFolderItems
    get() =
        listOf(
            LibraryFolderItem(
                label = UiText.StringResource(R.string.library_screen_favorites_label),
                icon = R.drawable.media_library_items_favorite,
                color = yellow,
                type = LibraryFolderType.FAVORITE_FOLDER,
            ),
            LibraryFolderItem(
                label = UiText.StringResource(R.string.library_screen_trash_label),
                icon = R.drawable.media_library_items_trash,
                color = red,
                type = LibraryFolderType.TRASH_FOLDER,
            ),
            LibraryFolderItem(
                label = UiText.StringResource(R.string.library_screen_secure_label),
                icon = R.drawable.media_library_items_secure,
                color = blue,
                type = LibraryFolderType.SECURE_FOLDER,
            ),
        )
