package com.hotaku.media.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R
import com.hotaku.media.model.MediaUi
import com.hotaku.media.utils.MediaType
import com.hotaku.ui.conposables.ShimmerPlaceHolder
import kotlinx.coroutines.delay
import java.time.Instant

@Composable
internal fun MediaPreviewScaffold(
    modifier: Modifier = Modifier,
    media: MediaUi,
    onOpenMedia: () -> Unit,
    onDeleteMedia: () -> Unit,
    onShareMedia: () -> Unit,
    onClosepreview: () -> Unit,
) {
    val isCompact = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    var showFloatBottomBar by remember { mutableStateOf(true) }

    LaunchedEffect(showFloatBottomBar) {
        if (showFloatBottomBar) {
            delay(1_500)
            showFloatBottomBar = false
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .noRippleClickable {
                    showFloatBottomBar = !showFloatBottomBar
                },
        contentAlignment = Alignment.Center,
    ) {
        when (media.mimeType) {
            MediaType.UNKNOWN -> {
                ShimmerPlaceHolder()
            }

            MediaType.IMAGE -> {
                Image(
                    itemUri = media.uriString,
                )
            }

            MediaType.VIDEO -> {
                Video(
                    itemUri = media.uriString,
                    onVideoClick = {
                        onOpenMedia()
                    },
                )
            }
        }
        AnimatedVisibility(
            visible = showFloatBottomBar,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier.fillMaxSize().statusBarsPadding(),
            ) {
                if (isCompact) {
                    IconButton(
                        modifier = Modifier.align(Alignment.TopStart),
                        onClick = onClosepreview,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Navigate back",
                            tint = Color.Gray,
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = onClosepreview,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close preview",
                            tint = Color.Gray,
                        )
                    }
                }
                ImagePreviewFloatBottomBar(
                    modifier = Modifier.align(Alignment.Center),
                    showPreviewButton = media.mimeType == MediaType.IMAGE,
                    onDeleteMedia = onDeleteMedia,
                    onShareMedia = onShareMedia,
                    onOpenMedia = onOpenMedia,
                )
            }
        }
    }
}

@Composable
private fun ImagePreviewFloatBottomBar(
    modifier: Modifier = Modifier,
    showPreviewButton: Boolean = false,
    onOpenMedia: () -> Unit,
    onDeleteMedia: () -> Unit,
    onShareMedia: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .padding(15.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = .5f))
                .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showPreviewButton) {
            IconButton(
                onClick = onOpenMedia,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.media_preview_full_screen),
                    contentDescription = "Full Screen",
                )
            }
        }
        IconButton(
            onClick = onShareMedia,
        ) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share Media",
            )
        }
        IconButton(
            onClick = onDeleteMedia,
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete Media",
            )
        }
    }
}

@Preview
@Composable
private fun MediapreviewPreview() {
    MiniGalleryTheme {
        MediaPreviewScaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            media =
                MediaUi(
                    mediaId = 7907,
                    uriString = "https://picsum.photos/200/300",
                    displayName = "Nola Gillespie",
                    mimeType = MediaType.VIDEO,
                    duration = "commune",
                    dateAdded = Instant.now(),
                    dateModified = Instant.now(),
                    size = 2566,
                    bucketDisplayName = "Ismael McCarthy",
                ),
            onOpenMedia = {},
            onDeleteMedia = {},
            onShareMedia = {},
            onClosepreview = {},
        )
    }
}
