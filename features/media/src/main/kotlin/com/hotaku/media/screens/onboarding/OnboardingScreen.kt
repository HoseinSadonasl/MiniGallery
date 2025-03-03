package com.hotaku.media.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.features.media.R
import com.hotaku.ui.conposables.OnScreenMessage

@Composable
internal fun OnboardingScreen(
    modifier: Modifier = Modifier,
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
    navigateToMediaScreen: () -> Unit,
) {
    if (permissionState) navigateToMediaScreen()
    OnboardingScreen(
        modifier = modifier,
        onRequestPermissions = { onRequestPermissions() },
    )
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onRequestPermissions: () -> Unit,
) {
    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            OnScreenMessage(
                modifier =
                    Modifier.fillMaxWidth(
                        fraction = if (windowWidth != WindowWidthSizeClass.COMPACT) .5f else 1f,
                    ),
                title = stringResource(id = com.hotaku.core_feature.ui.R.string.permissions_screen_message_title),
                fulMessage = stringResource(id = com.hotaku.core_feature.ui.R.string.permissions_screen_message),
            )

            FilledTonalButton(
                onClick = onRequestPermissions,
            ) {
                Text(
                    text = stringResource(R.string.onboarding_screen_grant_permissions_button_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun PermissionScreenPreview() {
    MiniGalleryTheme {
        OnboardingScreen { }
    }
}
