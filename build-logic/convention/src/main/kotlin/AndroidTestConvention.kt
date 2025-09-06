import com.android.build.gradle.TestExtension
import com.github.goldy1992.mp3player.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.withType

class AndroidTestConvention() : Plugin<Project> {
    override fun apply(target: Project) {
            with(target) {
                apply(plugin = "org.jetbrains.kotlin.android")
                val mockitoAgentDeclare by configurations.creating
                mockitoAgentDeclare.isCanBeDeclared = true
                mockitoAgentDeclare.isCanBeResolved= true
                mockitoAgentDeclare.isCanBeConsumed= true
                val mockitoAgentResolve by configurations.creating
                mockitoAgentResolve.extendsFrom(mockitoAgentDeclare)
                mockitoAgentResolve.isCanBeDeclared= false
                mockitoAgentResolve.isCanBeConsumed=false
                mockitoAgentResolve.isCanBeResolved = true

                dependencies {
                    val mockitoCore = libs.findLibrary("mockito.core").get()
                    add("testImplementation",mockitoCore)
                    add(mockitoAgentDeclare.name, mockitoCore)
                }
                tasks.withType<Test> {
                   jvmArgs.add("-javaagent:${mockitoAgentResolve.asPath}")
                }
            }
    }
}