package com.hotaku.media.screens.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.hotaku.features.media.R
import com.hotaku.ui.PermissionUtils.requiredMediaPermissions
import com.hotaku.ui.conposables.OnScreenMessage

@Composable
internal fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel,
    navigateToMediaListScreen: () -> Unit,
) {
    OnboardingScreen(
        modifier = modifier,
        viewModel = onboardingViewModel,
        onAction = onboardingViewModel::onAction,
        navigateToMediaListScreen = navigateToMediaListScreen,
    )
}

@Composable
private fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel,
    onAction: (OnboardingActions) -> Unit,
    navigateToMediaListScreen: () -> Unit,
) {
    val state by viewModel.onboardingState.collectAsStateWithLifecycle()

    val windowWidth = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { result ->
                result.entries.forEach { entry ->
                    entry.value.let {
                        viewModel.onAction(
                            OnboardingActions.OnRemovePermissionItemState(permission = entry.key),
                        )
                    }
                }
            },
        )

    LaunchedEffect(Unit) {
        viewModel.onAction(OnboardingActions.OnAddPermissionsToRequest(requiredMediaPermissions.asList()))
    }

    LaunchedEffect(state.mediaPermissions) {
        if (state.mediaPermissions?.isEmpty() == true) navigateToMediaListScreen()
    }

    LaunchedEffect(Unit) {
        viewModel.onboardingScreenEvent.collect { event ->
            when (event) {
                OnboardingScreenEvents.RequestPermissions -> {
                    state.mediaPermissions?.toTypedArray()?.let { permissionsArray ->
                        permissionLauncher.launch(
                            input = permissionsArray,
                        )
                    }
                }
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            OnScreenMessage(
                modifier =
                    Modifier
                        .fillMaxWidth(
                            fraction = if (windowWidth != WindowWidthSizeClass.COMPACT) .5f else 1f,
                        ),
                title = stringResource(id = com.hotaku.core_feature.ui.R.string.permissions_screen_message_title),
                fulMessage = stringResource(id = com.hotaku.core_feature.ui.R.string.permissions_screen_message),
            )

            FilledTonalButton(
                onClick = {
                    onAction(OnboardingActions.OnRequestPermissions)
                },
            ) {
                Text(
                    text = stringResource(R.string.onboarding_screen_grant_permissions_button_text),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
