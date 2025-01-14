package com.hotaku.minigallery.ui.screens.albums

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hotaku.minigallery.R
import com.hotaku.minigallery.ui.components.AppScaffold
import com.hotaku.minigallery.ui.components.MiniGalleryTopBar

@Composable
fun AlbumsScreen(modifier: Modifier = Modifier) {
    AppScaffold(
        modifier = modifier,
        topAppBar = {
            MiniGalleryTopBar(
                title = stringResource(R.string.albums_screen_top_app_bar_title),
            )
        },
    ) {
    }
}
