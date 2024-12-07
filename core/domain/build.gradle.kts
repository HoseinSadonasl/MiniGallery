plugins {
    alias(libs.plugins.minigallery.kotlin.library)
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.google.truth)
}
