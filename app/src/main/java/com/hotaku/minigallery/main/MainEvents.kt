package com.hotaku.minigallery.main

internal sealed interface MainEvents {
    data object RequestPermissions : MainEvents
}
