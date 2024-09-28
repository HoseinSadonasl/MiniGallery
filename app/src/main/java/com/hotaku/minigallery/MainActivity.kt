package com.hotaku.minigallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.minigallery.ui.navigation.MiniGalleryDestinations
import com.hotaku.minigallery.ui.screens.albums.AlbumsScreen
import com.hotaku.minigallery.ui.screens.home.HomeScreen
import com.hotaku.minigallery.ui.screens.settings.SettingsScreen
import com.hotaku.minigallery.ui.theme.MiniGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentDestination by rememberSaveable { mutableStateOf(MiniGalleryDestinations.HOME) }
            val adaptiveInfo = currentWindowAdaptiveInfo()
            val customNavSuiteType =
                with(adaptiveInfo) {
                    if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                        NavigationSuiteType.NavigationRail
                    } else {
                        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
                    }
                }
            val myNavigationSuiteItemColors =
                NavigationSuiteDefaults.itemColors(
                    navigationBarItemColors =
                        NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        ),
                )

            MiniGalleryTheme {
                NavigationSuiteScaffold(
                    navigationSuiteItems = {
                        MiniGalleryDestinations.entries.forEach { destination ->
                            item(
                                selected = destination == currentDestination,
                                icon = {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = stringResource(currentDestination.label),
                                    )
                                },
                                label = { Text(text = stringResource(destination.label)) },
                                onClick = { currentDestination = destination },
                                colors = myNavigationSuiteItemColors,
                            )
                        }
                    },
                    layoutType = customNavSuiteType,
                ) {
                    when (currentDestination) {
                        MiniGalleryDestinations.HOME -> HomeScreen()
                        MiniGalleryDestinations.ALBUMS -> AlbumsScreen()
                        MiniGalleryDestinations.SETTINGS -> SettingsScreen()
                    }
                } } }
    }
}
