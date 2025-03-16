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
        private val viewModelState = MutableStateFlow(OnboardingScreenState())
        val state = viewModelState.asStateFlow()

        private var viewModelEvent = Channel<OnboardingScreenEvents>()
        val event = viewModelEvent.receiveAsFlow()

        fun onAction(action: OnboardingActions) =
            when (action) {
                is OnAddPermissionsToRequest -> addPermissions(requiredMediaPermissions = action.permissions)
                is OnRemovePermissionItemState -> clearPermissionState(grantedPermission = action.permission)
                OnRequestPermissions -> requestPermissions()
            }

        private fun addPermissions(requiredMediaPermissions: List<String>) {
            viewModelState.update {
                it.copy(
                    mediaPermissions = requiredMediaPermissions,
                )
            }
        }

        private fun requestPermissions() {
            viewModelScope.launch {
                viewModelEvent.send(OnboardingScreenEvents.RequestPermissions)
            }
        }

        private fun clearPermissionState(grantedPermission: String) {
            viewModelState.update {
                it.copy(
                    mediaPermissions = it.mediaPermissions?.filterNot { permission -> permission == grantedPermission },
                )
            }
        }
    }
