plugins {
    alias(libs.plugins.minigallery.android.feature)
    alias(libs.plugins.minigallery.android.hilt)
}

android {
    namespace = "com.hotaku.home"
}

dependencies {

    libs.paging.compose
    libs.coil.compose

}