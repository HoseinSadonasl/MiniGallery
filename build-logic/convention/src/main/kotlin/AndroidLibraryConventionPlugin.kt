import com.android.build.gradle.LibraryExtension
import com.hotaku.minigallery.configureKotlinAndroid
import com.hotaku.minigallery.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply {
                plugin("com.android.library")
                plugin("org.jetbrains.kotlin.android")
                plugin("kotlin-parcelize")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                configureKotlinAndroid(this)
            }
        }
    }

}