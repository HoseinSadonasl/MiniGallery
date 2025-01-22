import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.ktlint.gradle) apply false
    alias(libs.plugins.detekt.gradle) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        ignoreFailures = false
        verbose = true
        debug.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
        }
    }

    configure<DetektExtension> {
        config.from(rootProject.files("config/detekt/detekt.yml"))
        ignoredBuildTypes = listOf("release")
        buildUponDefaultConfig = true
        allRules = false
        autoCorrect = true
        parallel = true
    }

    tasks.withType<Detekt> {
        reports {
            html.required = true
            txt.required = false
            md.required = false
            xml.required = false
            sarif.required = false
        }
    }
}
