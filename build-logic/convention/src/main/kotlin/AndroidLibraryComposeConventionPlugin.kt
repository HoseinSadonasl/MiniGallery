import com.android.build.gradle.LibraryExtension
import com.hotaku.minigallery.configureComposeAndroid
import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply {
                plugin("minigallery.android.library")
                plugin("org.jetbrains.kotlin.plugin.compose")
            }
            extensions.configure<LibraryExtension> {
                configureComposeAndroid(this)
            }
        }
    }

}