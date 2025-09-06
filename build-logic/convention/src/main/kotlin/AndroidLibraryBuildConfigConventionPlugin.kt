
import com.android.build.api.dsl.LibraryExtension
import com.github.goldy1992.mp3player.configureBuildConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryBuildConfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.getByType<LibraryExtension>()
            // For Android 36, target JVM 21
            extensions.getByType<JavaPluginExtension>().apply {
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(21))
                }
            }

            extensions.getByType<KotlinAndroidProjectExtension>().apply {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
            }

            configureBuildConfig(extension)
        }
    }

}