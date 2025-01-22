plugins {
    alias(libs.plugins.minigallery.kotlin.library)
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.paging.common)
    implementation(libs.hilt.core)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.google.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.cash.turbine)
}
