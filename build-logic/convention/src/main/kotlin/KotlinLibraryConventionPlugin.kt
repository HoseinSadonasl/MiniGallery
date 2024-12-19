import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin")
                apply("com.google.devtools.ksp")
            }
            dependencies {
                add("implementation", libs.findLibrary("coroutines.core").get())
                add("implementation", libs.findLibrary("hilt.core").get())
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("javax-inject").get())
            }
        }
    }
}