import com.android.build.gradle.LibraryExtension
import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                testOptions.unitTests.isIncludeAndroidResources = true
            }
            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("androidx.junit").get())
                add("testImplementation", libs.findLibrary("mockk.android").get())
                add("testImplementation", libs.findLibrary("mockk.agent").get())
                add("testImplementation", libs.findLibrary("google.truth").get())
                add("testImplementation", libs.findLibrary("coroutines.test").get())
                add("testImplementation", libs.findLibrary("cash.turbine").get())
            }
        }
    }
}