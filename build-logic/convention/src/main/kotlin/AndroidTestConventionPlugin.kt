import com.android.build.gradle.LibraryExtension
import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                testOptions.unitTests.isIncludeAndroidResources = true
            }
            dependencies {
                val bom = libs.findLibrary("androidx.compose.bom").get()
                add("androidTestImplementation", platform(bom))
                add("androidTestImplementation", libs.findLibrary("junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.espresso.core").get())
                add("androidTestImplementation", libs.findLibrary("androidx-ui-test-manifest").get())
                add("androidTestImplementation", libs.findLibrary("androidx-ui-test-junit4").get())
            }
        }
    }
}