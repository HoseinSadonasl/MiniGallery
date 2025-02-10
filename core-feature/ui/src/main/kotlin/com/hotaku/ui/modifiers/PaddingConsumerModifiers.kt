package com.hotaku.ui.modifiers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier

fun Modifier.dynamicPaddingConsume(
    consume: Boolean = true,
    paddingValues: PaddingValues,
): Modifier =
    if (consume) {
        this.consumeWindowInsets(paddingValues)
    } else {
        this.padding(paddingValues)
    }

fun Modifier.dynamicStatusBarPadding(addPadding: Boolean = true): Modifier = if (addPadding) this.statusBarsPadding() else this
