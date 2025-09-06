import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

class TestConventionPlugin() : Plugin<Project> {
    override fun apply(target: Project) {
       with(target) {
           tasks.withType<Test> {
               failOnNoDiscoveredTests.set(false)
           }
       }
    }
}
