package com.hotaku.ui.conposables

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = null,
        indication = null,
        onClick = onClick,
    )
}
