package com.hotaku.onboarding

internal sealed interface OnboardingActions {
    data class OnAddPermissionsToRequest(val permissions: List<String>) : OnboardingActions

    data class OnRemovePermissionItemState(val permission: String) : OnboardingActions

    data object OnRequestPermissions : OnboardingActions
}
