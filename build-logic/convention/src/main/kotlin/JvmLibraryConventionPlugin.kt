import com.android.build.gradle.LibraryExtension
import com.hotaku.minigallery.configureKotlinAndroid
import com.hotaku.minigallery.configureKotlinJvm
import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.JvmLibrary
import org.gradle.kotlin.dsl.configure

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