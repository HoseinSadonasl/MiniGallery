plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.android.room)
}

android {
    namespace = "com.hotaku.media.media_datasource"
}

dependencies {

    implementation(projects.media.mediaData)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
}
