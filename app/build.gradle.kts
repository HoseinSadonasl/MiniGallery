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

    implementation(project(":feature:home"))
    implementation(project(":feature:albums"))
    implementation(project(":libraries:core-ui"))
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

}