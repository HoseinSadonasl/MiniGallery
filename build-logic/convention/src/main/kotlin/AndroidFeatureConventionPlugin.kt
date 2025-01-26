import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("minigallery.android.library.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
                add("implementation", libs.findLibrary("androidx.activity.compose").get())
                add("implementation", libs.findLibrary("androidx.compose.bom").get())
                add("implementation", libs.findLibrary("androidx.ui").get())
                add("implementation", libs.findLibrary("androidx.ui.graphics").get())
                add("implementation", libs.findLibrary("androidx.ui.tooling").get())
                add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
                add("implementation", libs.findLibrary("androidx.material3").get())
                add("implementation", libs.findLibrary("kotlinx.serialization.json").get())

                add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
                add("debugImplementation", libs.findLibrary("androidx.ui.tooling.preview").get())

                add("implementation", libs.findLibrary("navigation.compose").get())
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("coil.compose").get())
            }
        }
    }

}