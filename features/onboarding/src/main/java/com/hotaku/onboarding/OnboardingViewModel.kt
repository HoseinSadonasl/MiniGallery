package com.hotaku.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotaku.onboarding.OnboardingActions.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class OnboardingViewModel
    @Inject
    constructor() : ViewModel() {
        private val onboardingViewModelState = MutableStateFlow(OnboardingScreenState())
        val onboardingState = onboardingViewModelState.asStateFlow()

        private var mainViewModelEvent = Channel<OnboardingScreenEvents>()
        val onboardingScreenEvent = mainViewModelEvent.receiveAsFlow()

        fun onAction(action: OnboardingActions) =
            when (action) {
                is OnAddPermissionsToRequest -> addPermissions(requiredMediaPermissions = action.permissions)
                is OnRemovePermissionItemState -> clearPermissionState(grantedPermission = action.permission)
                OnRequestPermissions -> requestPermissions()
            }

        private fun addPermissions(requiredMediaPermissions: List<String>) {
            onboardingViewModelState.update {
                it.copy(
                    mediaPermissions = requiredMediaPermissions,
                )
            }
        }

        private fun requestPermissions() {
            viewModelScope.launch {
                mainViewModelEvent.send(OnboardingScreenEvents.RequestPermissions)
            }
        }

        private fun clearPermissionState(grantedPermission: String) {
            onboardingViewModelState.update {
                it.copy(
                    mediaPermissions = it.mediaPermissions?.filterNot { permission -> permission == grantedPermission },
                )
            }
        }
    }
