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
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    implementation(projects.core.common)
    implementation(projects.core.domain)
//    implementation(projects.libraries)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.feature.home)
    implementation(projects.feature.albums)
    implementation(projects.libraries.designsystem)
    implementation(projects.libraries.navigation)
    implementation(projects.media.mediaDatasource)
    implementation(projects.media.mediaData)
    implementation(projects.media.mediaDomain)

    testImplementation(libs.junit)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.google.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
