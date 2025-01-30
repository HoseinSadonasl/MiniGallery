package com.hotaku.minigallery.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel
    @Inject
    constructor() : ViewModel() {
        private val mainViewModelState = MutableStateFlow(MainAppState())
        val mainActivityState = mainViewModelState.asStateFlow()

        private var mainViewModelEvent = Channel<MainEvents>()
        val mainActivityEvent = mainViewModelEvent.receiveAsFlow()

        fun onAction(action: MainActions) =
            when (action) {
                is MainActions.OnAddPermissions -> addPermissions(action.permissions)
                is MainActions.OnRemovePermissionItemState -> clearPermissionState(action.permission)
                MainActions.OnRequestPermissions -> requestPermissions()
            }

        private fun addPermissions(permissions: List<String>) {
            mainViewModelState.update {
                it.copy(
                    permissions = permissions,
                )
            }
        }

        private fun requestPermissions() {
            viewModelScope.launch {
                mainViewModelEvent.send(MainEvents.RequestPermissions)
            }
        }

        private fun clearPermissionState(permission: String) {
            mainViewModelState.update {
                it.copy(
                    permissions = it.permissions.filterNot { it == permission },
                )
            }
        }
    }
