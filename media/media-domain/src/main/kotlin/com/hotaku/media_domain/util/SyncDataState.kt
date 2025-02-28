package com.hotaku.media_domain.util

sealed interface SyncDataState {
    data object Idle : SyncDataState

    data object Syncing : SyncDataState

    data class SyncFailure(val reason: SyncFailureReason) : SyncDataState

    data class SyncSuccess(val itemsCount: Int) : SyncDataState
}
