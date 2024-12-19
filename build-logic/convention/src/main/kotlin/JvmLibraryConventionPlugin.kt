import com.hotaku.minigallery.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply {
                plugin("org.jetbrains.kotlin.jvm")
            }
            configureKotlinJvm()
        }
    }

}