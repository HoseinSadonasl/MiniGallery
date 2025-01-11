plugins {
    alias(libs.plugins.minigallery.kotlin.library)
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.google.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.cash.turbine)
}
