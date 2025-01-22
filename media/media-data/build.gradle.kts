plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.test.library)
    alias(libs.plugins.minigallery.test.android)
    alias(libs.plugins.minigallery.test.hilt)
}

android {
    namespace = "com.hotaku.media.media_data"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(projects.core.domain)
    implementation(projects.media.mediaDomain)
    implementation(projects.core.common)
    implementation(libs.paging.common)
    implementation(libs.androidx.work)
    implementation(libs.hilt.work)
}
