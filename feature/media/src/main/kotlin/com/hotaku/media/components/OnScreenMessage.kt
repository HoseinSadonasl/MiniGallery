package com.hotaku.media.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R

@Composable
fun OnScreenMessage(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    title: String,
    fulMessage: String,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val maxWidth =
        when (adaptiveInfo.windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> 1f
            WindowWidthSizeClass.MEDIUM -> .5f
            else -> .3f
        }
    Column(
        modifier = modifier.fillMaxWidth(maxWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier =
                Modifier
                    .fillMaxWidth(.7f)
                    .aspectRatio(1f)
                    .alpha(.5f),
            imageVector = ImageVector.vectorResource(id = R.drawable.all_media_illustration),
            tint = color,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = title,
            textAlign = TextAlign.Center,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color,
                ),
        )
        Text(
            modifier = Modifier.fillMaxWidth(.8f),
            text = fulMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = color,
        )
    }
}

@PreviewLightDark
@Composable
private fun ScreenMessagePreview() {
    MiniGalleryTheme {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            OnScreenMessage(
                title = stringResource(id = R.string.permissions_screen_message_title),
                fulMessage = stringResource(id = R.string.permissions_screen_message),
            )
        }
    }
}
