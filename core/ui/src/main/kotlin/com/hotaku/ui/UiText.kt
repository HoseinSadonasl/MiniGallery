package com.hotaku.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hotaku.ui.UiText.DynamicString
import com.hotaku.ui.UiText.StringResource

sealed interface UiText {
    data class StringResource(val resId: Int) : UiText

    data class DynamicString(val value: String) : UiText
}

@Composable
fun UiText.asString(): String {
    val context = LocalContext.current
    return when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId)
    }
}

fun UiText.asString(context: Context): String {
    return when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId)
    }
}

infix fun String?.or(resId: Int): UiText {
    return when {
        this.isNullOrBlank() -> {
            StringResource(resId)
        }

        else -> {
            DynamicString(this)
        }
    }
}
