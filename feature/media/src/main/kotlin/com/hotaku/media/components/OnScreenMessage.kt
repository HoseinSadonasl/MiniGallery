package com.hotaku.media.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
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
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        MessageSection(
            windowSize = windowSize,
            color = color,
            title = title,
            fulMessage = fulMessage,
        )
    }
}

@Composable
private fun MessageSection(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    color: Color,
    title: String,
    fulMessage: String,
) {
    val iconFraction =
        when (windowSize) {
            WindowWidthSizeClass.COMPACT -> .7f
            WindowWidthSizeClass.MEDIUM -> .5f
            else -> .15f
        }
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = iconFraction)
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
@PreviewScreenSizes
@Composable
private fun ScreenMessagePreview() {
    MiniGalleryTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            OnScreenMessage(
                modifier = Modifier.align(Alignment.Center),
                title = stringResource(id = R.string.permissions_screen_message_title),
                fulMessage = stringResource(id = R.string.permissions_screen_message),
            )
        }
    }
}
