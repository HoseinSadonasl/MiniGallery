package com.hotaku.minigallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hotaku.designsystem.theme.Colors
import com.hotaku.designsystem.theme.LocalMiniGalleryColors
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.navigation.SuiteNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            MiniGalleryTheme {
                CompositionLocalProvider(LocalMiniGalleryColors provides Colors()) {
                    Scaffold { padding ->
                        SuiteNav(
                            modifier = Modifier.padding(padding),
                            navHostController = navHostController,
                        )
                    }
                }
            }
        }
    }
}
