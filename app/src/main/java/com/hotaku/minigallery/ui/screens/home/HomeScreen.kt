package com.hotaku.minigallery.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.hotaku.minigallery.R
import com.hotaku.minigallery.ui.components.AppScaffold
import com.hotaku.minigallery.ui.components.MiniGalleryTopBar
import com.hotaku.minigallery.ui.utils.content.Image
import com.hotaku.minigallery.ui.utils.content.imagesRetriever

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var images = remember { mutableListOf<Image>() }

    LaunchedEffect(true) {
        images = context.imagesRetriever().toMutableList()
    }

    AppScaffold(
        modifier = modifier,
        topAppBar = {
            MiniGalleryTopBar(
                title = stringResource(R.string.home_screen_top_app_bar_title),
            )
        },
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(images) { image: Image ->
                Column(
                ) {
                    AsyncImage(
                        model = image.uri,
                        contentDescription = null
                    )
                    image.imageName?.let { Text(text = it) }
                }
            }
        }
    }
}