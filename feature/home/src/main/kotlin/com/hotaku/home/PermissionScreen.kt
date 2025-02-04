package com.hotaku.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.feature.home.R
import com.hotaku.home.components.ScreenMessage

@Composable
internal fun PermissionsScreen(
    modifier: Modifier = Modifier,
    permissionState: Boolean,
    onRequestPermissions: () -> Unit,
    navigateToHomeScreen: () -> Unit,
) {
    if (permissionState) navigateToHomeScreen()
    PermissionsScreen(
        modifier = modifier,
        onRequestPermissions = { onRequestPermissions() },
    )
}

@Composable
private fun PermissionsScreen(
    modifier: Modifier = Modifier,
    onRequestPermissions: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            ScreenMessage(
                title = stringResource(id = R.string.permissions_screen_message_title),
                fulMessage = stringResource(id = R.string.permissions_screen_message),
            )

            FilledTonalButton(
                onClick = onRequestPermissions,
            ) {
                Text(
                    text = stringResource(R.string.permissions_screen_grant_permissions_button_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PermissionScreenPreview() {
    MiniGalleryTheme {
        PermissionsScreen { }
    }
}
