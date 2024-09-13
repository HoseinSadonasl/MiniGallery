import com.android.build.api.dsl.ApplicationExtension
import com.hotaku.minigallery.configureComposeAndroid
import com.hotaku.minigallery.configureKotlinAndroid
import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply {
                plugin("com.android.application")
                plugin("org.jetbrains.kotlin.plugin.compose")
            }
            extensions.configure<ApplicationExtension> {
                configureComposeAndroid(libs,this)
            }
        }
    }
}