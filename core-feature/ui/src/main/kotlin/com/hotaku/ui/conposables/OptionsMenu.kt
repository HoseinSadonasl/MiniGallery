package com.hotaku.ui.conposables

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties

@Composable
fun OptionsMenu(
    modifier: Modifier = Modifier,
    expend: Boolean,
    node: @Composable () -> Unit,
    options: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
) {
    OptionsMenuImpl(
        modifier = modifier,
        expend = expend,
        nodeButton = node,
        options = options,
        onDismissRequest = onDismissRequest,
    )
}

@Composable
private fun OptionsMenuImpl(
    modifier: Modifier = Modifier,
    expend: Boolean,
    nodeButton: @Composable () -> Unit,
    options: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        nodeButton()
        DropdownMenu(
            expanded = expend,
            onDismissRequest = onDismissRequest,
            shape = MaterialTheme.shapes.medium,
            properties =
                PopupProperties(
                    focusable = true,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = true,
                ),
            content = { options() },
        )
    }
}

@Composable
fun OptionMenuItem(
    option: String,
    onMenuItemClick: (String) -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(text = option)
        },
        onClick = { onMenuItemClick(option) },
    )
}
