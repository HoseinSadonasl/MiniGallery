package com.hotaku.minigallery.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hotaku.minigallery.R
import com.hotaku.minigallery.ui.components.AppScaffold
import com.hotaku.minigallery.ui.components.MiniGalleryTopBar

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    AppScaffold(
        modifier = modifier,
        topAppBar = {
            MiniGalleryTopBar(
                title = stringResource(R.string.home_screen_top_app_bar_title),
            )
        },
    ) {

    }
}