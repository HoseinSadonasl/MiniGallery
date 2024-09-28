plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.test.library)
}

android {
    namespace = "com.hotaku.common"
}

dependencies {

    implementation(libs.androidx.core.ktx)
}
