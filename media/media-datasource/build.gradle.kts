plugins {
    alias(libs.plugins.minigallery.android.library)
    alias(libs.plugins.minigallery.android.hilt)
    alias(libs.plugins.minigallery.android.room)
    alias(libs.plugins.minigallery.test.library)
}

android {
    namespace = "com.hotaku.media.media_datasource"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.database)
    implementation(projects.media.mediaData)
    implementation(libs.androidx.core.ktx)
}
