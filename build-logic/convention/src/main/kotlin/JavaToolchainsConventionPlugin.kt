import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import kotlin.text.set

class JavaToolchainsConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions["Java"]
        val javaExtension = target.extensions.getByType<JavaPluginExtension>()
        javaExtension.toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }


}