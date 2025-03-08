package com.hotaku.ui.conposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hotaku.ui.MediaType
import com.hotaku.ui.models.MediaUi

@Composable
fun MediaDetail(
    modifier: Modifier = Modifier,
    showFloatOptions: Boolean,
    media: MediaUi,
    floatOptions: @Composable () -> Unit,
) {
    MediaDetailImpl(
        modifier = modifier,
        showFloatOptions = showFloatOptions,
        media = media,
        floatOptions = floatOptions,
    )
}

@Composable
private fun MediaDetailImpl(
    modifier: Modifier = Modifier,
    showFloatOptions: Boolean,
    media: MediaUi,
    floatOptions: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (media.mimeType) {
            MediaType.UNKNOWN -> {
                MediaPlaceHolder()
            }

            MediaType.IMAGE -> {
                Image(itemUri = media.uriString)
            }

            MediaType.VIDEO -> {
                Video(itemUri = media.uriString)
            }
        }
        AnimatedVisibility(
            visible = showFloatOptions,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier
                    .statusBarsPadding(),
            ) {
                floatOptions()
            }
        }
    }
}
