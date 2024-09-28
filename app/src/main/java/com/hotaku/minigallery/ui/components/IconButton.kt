package com.hotaku.minigallery.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MinimalIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    IconButton(
        modifier =
            Modifier
                .padding(4.dp)
                .clip(shape = MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}
