plugins {
    alias(libs.plugins.minigallery.android.feature)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.test.library)
    alias(libs.plugins.minigallery.test.android)
}

android {
    namespace = "com.hotaku.core_feature.ui"
}

dependencies {

    implementation(projects.coreFeature.designsystem)
    implementation(libs.paging.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.constraintlayout)
}
