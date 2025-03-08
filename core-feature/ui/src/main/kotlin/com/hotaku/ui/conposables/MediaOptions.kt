package com.hotaku.ui.conposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MediaOptions(
    modifier: Modifier = Modifier,
    onShareMedia: () -> Unit,
    onDeleteMedia: () -> Unit,
    extraActions: @Composable RowScope.() -> Unit = {},
    moreAction: (() -> Unit)? = null,
) {
    MediaOptionsImpl(
        modifier = modifier,
        onShareMedia = onShareMedia,
        onDeleteMedia = onDeleteMedia,
        extraActions = extraActions,
        moreAction = moreAction,
    )
}

@Composable
private fun MediaOptionsImpl(
    modifier: Modifier = Modifier,
    onShareMedia: () -> Unit,
    onDeleteMedia: () -> Unit,
    extraActions: @Composable RowScope.() -> Unit = {},
    moreAction: (() -> Unit)? = null,
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
        moreAction?.let {
            IconButton(
                onClick = it,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "More",
                )
            }
        }
    }
}
