plugins {
    alias(libs.plugins.minigallery.kotlin.library)
}

dependencies {
    implementation(libs.paging.common)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.google.truth)
    testImplementation(libs.coroutines.test)
}
