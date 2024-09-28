plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.test.library)
}

android {
    namespace = "com.hotaku.datastore"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.preferences.data.store)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}
