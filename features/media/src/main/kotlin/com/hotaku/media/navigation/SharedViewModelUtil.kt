package com.hotaku.media.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
internal inline fun <reified T : ViewModel> NavBackStackEntry.sharedHiltViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel<T>()
    val parentEntry =
        remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
    return hiltViewModel(
        viewModelStoreOwner = parentEntry,
    )
}
