plugins {
    alias(libs.plugins.minigallery.android.library.compose)
}

android {
    namespace = "com.hotaku.core_feature.navigation"
}

dependencies {
    implementation(projects.features.media)
    implementation(projects.features.mediaLibrary)
    implementation(projects.features.mediaDetails)
    implementation(projects.features.albums)
    implementation(projects.features.onboarding)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
}
