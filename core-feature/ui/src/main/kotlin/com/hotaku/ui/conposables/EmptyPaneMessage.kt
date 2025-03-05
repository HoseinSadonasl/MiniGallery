package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EmptyPaneMessage(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    message: String,
) {
    EmptyPaneMessageImpl(
        modifier = modifier,
        icon = icon,
        message = message,
    )
}

@Composable
private fun EmptyPaneMessageImpl(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    message: String,
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = icon,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = "Message Icon",
            )
            Spacer(Modifier.width(8.dp))
            Text(text = message, style = MaterialTheme.typography.labelLarge)
        }
    }
}
