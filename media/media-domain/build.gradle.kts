plugins {
    alias(libs.plugins.minigallery.kotlin.library)
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.google.truth)
    testImplementation(libs.coroutines.test)
}
