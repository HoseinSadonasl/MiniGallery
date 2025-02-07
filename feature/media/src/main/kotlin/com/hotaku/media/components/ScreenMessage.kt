package com.hotaku.media.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.media.R

@Composable
fun ScreenMessage(
    modifier: Modifier = Modifier,
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
        DecorationMediaImage(
            image = painterResource(R.drawable.all_media_illustration),
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = title,
            textAlign = TextAlign.Center,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                ),
        )
        Text(
            modifier = Modifier.fillMaxWidth(.8f),
            text = fulMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@PreviewLightDark
@Composable
private fun ScreenMessagePreview() {
    MiniGalleryTheme {
        ScreenMessage(
            title = stringResource(id = R.string.permissions_screen_message_title),
            fulMessage = stringResource(id = R.string.permissions_screen_message),
        )
    }
}
