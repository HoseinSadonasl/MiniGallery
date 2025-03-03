package com.hotaku.ui.conposables

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.ui.MediaType
import com.hotaku.ui.models.MediaUi
import kotlinx.coroutines.delay
import java.time.Instant

@Composable
fun MediaDetail(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    media: MediaUi,
    onPlayVideo: () -> Unit,
    onClose: () -> Unit,
    floatOptions: @Composable () -> Unit,
) {
    var showFloatOptions by remember { mutableStateOf(true) }

    LaunchedEffect(showFloatOptions) {
        if (showFloatOptions) {
            delay(1_500)
            showFloatOptions = false
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .noRippleClickable {
                    showFloatOptions = !showFloatOptions
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
                        onPlayVideo()
                    },
                )
            }
        }
        AnimatedVisibility(
            visible = showFloatOptions,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier.fillMaxSize().statusBarsPadding(),
            ) {
                if (isCompact) {
                    IconButton(
                        modifier = Modifier.align(Alignment.TopStart),
                        onClick = onClose,
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
                        onClick = onClose,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close preview",
                            tint = Color.Gray,
                        )
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    floatOptions()
                }
            }
        }
    }
}

@Composable
fun MediaOptions(
    modifier: Modifier = Modifier,
    onShareMedia: () -> Unit,
    onDeleteMedia: () -> Unit,
    extraActions: @Composable () -> Unit = {},
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
        extraActions()
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
        MediaDetail(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            isCompact = false,
            media =
                MediaUi(
                    mediaId = 7907,
                    uriString = "https://picsum.photos/200/300",
                    displayName = "Nola Gillespie",
                    mimeType = MediaType.VIDEO,
                    duration = 213343,
                    dateAdded = Instant.now(),
                    dateModified = Instant.now(),
                    size = 2566,
                    bucketDisplayName = "Ismael McCarthy",
                ),
            onPlayVideo = {},
            onClose = {},
            floatOptions = {},
        )
    }
}
