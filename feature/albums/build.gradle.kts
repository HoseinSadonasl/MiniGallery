plugins {
    alias(libs.plugins.minigallery.android.feature)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.test.library)
    alias(libs.plugins.minigallery.test.android)
}

android {
    namespace = "com.hotaku.albums"
}

dependencies {

    implementation(libs.paging.compose)
    implementation(libs.coil.compose)
}
