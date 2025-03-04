package com.hotaku.media.screens.onboarding

internal sealed interface OnboardingScreenEvents {
    data object RequestPermissions : OnboardingScreenEvents
}
