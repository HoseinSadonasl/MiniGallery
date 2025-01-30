package com.hotaku.minigallery.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hotaku.designsystem.theme.Colors
import com.hotaku.designsystem.theme.LocalMiniGalleryColors
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.minigallery.utils.PermissionUtils
import com.hotaku.navigation.AppSuiteNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewmodel: MainViewModel by viewModels()

    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { result ->
            result.entries.forEach { entry ->
                entry.value.let {
                    viewmodel.onAction(
                        MainActions.OnRemovePermissionItemState(permission = entry.key),
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Access media permissions to request (if permissions was granted it will be empty)
        val permissions: List<String> = PermissionUtils.permissionsToRequest(this)
        addPermissionsTorequest(permissions)

        splashScreen.setKeepOnScreenCondition { false }
        setContent {
            val navHostController: NavHostController = rememberNavController()
            val mainState by viewmodel.mainActivityState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewmodel.mainActivityEvent.collect { event ->
                    when (event) {
                        MainEvents.RequestPermissions -> {
                            requestPermissionLauncher.launch(
                                input = mainState.permissions.toTypedArray(),
                            )
                        }
                    }
                }
            }

            MiniGalleryTheme {
                CompositionLocalProvider(LocalMiniGalleryColors provides Colors()) {
                    AppSuiteNav(
                        navHostController = navHostController,
                        permissionState = mainState.permissions.isEmpty(),
                        onRequestPermissions = {
                            viewmodel.onAction(
                                MainActions.OnRequestPermissions,
                            )
                        },
                    )
                }
            }
        }
    }

    private fun addPermissionsTorequest(permissions: List<String>) {
        viewmodel.onAction(
            MainActions.OnAddPermissions(
                permissions = permissions,
            ),
        )
    }
}
