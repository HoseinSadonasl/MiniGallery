plugins {
    alias(libs.plugins.minigallery.android.feature)
    alias(libs.plugins.minigallery.android.hilt)
}

android {
    namespace = "com.hotaku.albums"
}

dependencies {

    implementation(libs.paging.compose)
    implementation(libs.coil.compose)

}