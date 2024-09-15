package com.hotaku.minigallery

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureComposeAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
    }
    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        add("implementation", platform(bom))
    }
}