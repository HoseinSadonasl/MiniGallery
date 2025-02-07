plugins {
    alias(libs.plugins.minigallery.android.feature)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.test.library)
    alias(libs.plugins.minigallery.test.android)
}

android {
    namespace = "com.hotaku.feature.home"
}

dependencies {

    implementation(projects.media.mediaDomain)
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.coreFeature.ui)
    implementation(projects.coreFeature.designsystem)
    implementation(libs.paging.compose)
    implementation(libs.coil.compose)
}
