package com.hotaku.media.components

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

internal fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = null,
        indication = null,
        onClick = onClick,
    )
}
