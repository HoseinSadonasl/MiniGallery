plugins {
    alias(libs.plugins.minigallery.android.library.compose)
}

android {
    namespace = "com.hotaku.libraries.navigation"
}

dependencies {
    implementation(projects.feature.home)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
}
