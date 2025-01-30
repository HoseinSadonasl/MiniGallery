package com.hotaku.minigallery.main

internal sealed interface MainActions {
    data class OnAddPermissions(val permissions: List<String>) : MainActions

    data class OnRemovePermissionItemState(val permission: String) : MainActions

    data object OnRequestPermissions : MainActions
}
