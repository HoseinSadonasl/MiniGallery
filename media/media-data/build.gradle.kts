plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
}

android {
    namespace = "com.hotaku.data"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)

}