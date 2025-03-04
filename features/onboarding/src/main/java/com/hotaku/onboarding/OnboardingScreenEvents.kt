package com.hotaku.onboarding

internal sealed interface OnboardingScreenEvents {
    data object RequestPermissions : OnboardingScreenEvents
}
