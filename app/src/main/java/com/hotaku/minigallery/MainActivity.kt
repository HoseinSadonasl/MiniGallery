package com.hotaku.minigallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hotaku.designsystem.theme.Colors
import com.hotaku.designsystem.theme.LocalMiniGalleryColors
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.navigation.AppSuiteNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController: NavHostController = rememberNavController()
            MiniGalleryTheme {
                CompositionLocalProvider(LocalMiniGalleryColors provides Colors()) {
                    AppSuiteNav(
                        navHostController = navHostController,
                    )
                }
            }
        }
    }
}
