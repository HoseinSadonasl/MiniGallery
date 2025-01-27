package com.hotaku.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.LocalMiniGalleryColors
import com.hotaku.designsystem.theme.MiniGalleryTheme

@Composable
internal fun MediaSyncLabel(
    modifier: Modifier = Modifier,
    isSyncing: Boolean = false,
    backgroundColor: Color,
    icon: ImageVector? = null,
    label: String,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(backgroundColor)
                .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSyncing) {
            CircularProgressIndicator(
                modifier = Modifier.size(8.dp),
                color = MaterialTheme.colorScheme.surface,
                strokeWidth = 1.dp,
            )
        }
        icon?.let {
            Icon(
                modifier = Modifier.size(8.dp),
                imageVector = it,
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = null,
            )
        }

        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surface,
        )
    }
}

@PreviewLightDark()
@Composable
private fun MediaSyncLabelPreview() {
    MiniGalleryTheme {
        MediaSyncLabel(
            backgroundColor = LocalMiniGalleryColors.current.green,
            isSyncing = true,
            icon = Icons.Default.Build,
            label = "Text",
        )
    }
}
