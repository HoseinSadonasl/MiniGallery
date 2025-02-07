plugins {
    alias(libs.plugins.minigallery.android.library.compose)
}

android {
    namespace = "com.hotaku.core_feature.navigation"
}

dependencies {
    implementation(projects.feature.media)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
}
