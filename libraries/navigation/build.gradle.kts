plugins {
    alias(libs.plugins.minigallery.android.library.compose)
}

android {
    namespace = "com.hotaku.navigation"
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}