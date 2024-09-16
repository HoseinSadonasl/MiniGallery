import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.hotaku.minigallery.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.androidx.room.gradle.plugin)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

gradlePlugin {
    plugins {
        register("miniGalleryAndroidApplication") {
            id = "minigallery.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("miniGalleryAndroidComposeApplication") {
            id = "minigallery.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("miniGalleryFeatureConvention") {
            id = "minigallery.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("miniGalleryHiltConvention") {
            id = "minigallery.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("miniGallerylibraryComposeConvention") {
            id = "minigallery.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("miniGallerylibraryConvention") {
            id = "minigallery.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("miniGalleryRoomConvention") {
            id = "minigallery.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("miniGalleryJvmlibraryConvention") {
            id = "minigallery.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}