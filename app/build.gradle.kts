plugins {
    alias(libs.plugins.minigallery.android.application)
    alias(libs.plugins.minigallery.android.application.compose)
    alias(libs.plugins.minigallery.android.hilt)
}

android {
    namespace = "com.hotaku.minigallery"

    defaultConfig {
        applicationId = "com.hotaku.minigallery"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.coil.compose)

}