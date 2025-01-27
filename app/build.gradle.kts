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
    implementation(project(":feature:home"))
    implementation(project(":feature:albums"))
    implementation(project(":libraries:navigation"))
    implementation(project(":libraries:designsystem"))
    implementation(project(":media:media-domain"))
    implementation(project(":media:media-data"))
    implementation(project(":core:database"))
    implementation(project(":media:media-datasource"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.coil.compose)

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
