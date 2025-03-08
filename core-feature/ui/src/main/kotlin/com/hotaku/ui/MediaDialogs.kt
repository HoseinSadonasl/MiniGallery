package com.hotaku.ui

sealed interface MediaDialogs {
    data object Idle : MediaDialogs

    data object RenameMediaDialog : MediaDialogs
}
