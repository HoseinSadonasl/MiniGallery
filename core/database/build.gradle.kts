plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.android.room)
    alias(libs.plugins.minigallery.test.android)
}

android {
    namespace = "com.hotaku.core.database"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.coroutines.android)
}
