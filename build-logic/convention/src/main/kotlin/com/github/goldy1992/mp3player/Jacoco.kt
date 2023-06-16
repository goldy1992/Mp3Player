import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

private const val JACOCO_GRADLE_TASK_GROUP = "Reporting"

private val coverageExclusions = listOf (
    "**/R.class",
    "**/R\$*.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "**/ui/**", // ui coverage done in Android Tests
    "android/**/*.*",
    "**/dagger/**",
    "**/databinding/**",
    "**/hilt_aggregated_deps/**",
    "**/Hilt_**"
)

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

internal fun Project.configureJacoco(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }
    val jacocoTestReport = tasks.create("jacocoTestReport")
    jacocoTestReport.group = JACOCO_GRADLE_TASK_GROUP

    androidComponentsExtension.onVariants { variant ->
        val debugPrefix = "project: ${this@configureJacoco.project.name}, variant: ${variant.name}"
        logger.trace("configureJacoco androidComponentExtension.onVariants invokes for $debugPrefix")
        val testTaskName = "test${variant.name.capitalize()}UnitTest"
        val testJacocoExecPath = "$buildDir/outputs/unit_test_code_coverage/${variant.name}UnitTest/test${variant.name.capitalize()}UnitTest.exec"
        logger.debug("configureJacoco androidComponentExtension.onVariants invokes for $debugPrefix execution data path: $testJacocoExecPath")
        val taskName = "jacoco${testTaskName.capitalize()}Report"


        val reportTask = tasks.register(
            name = taskName,
            type = JacocoReport::class
        ) {
            dependsOn(testTaskName)
            group = JACOCO_GRADLE_TASK_GROUP
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
            val mainClasses = fileTree( "${buildDir}/tmp/kotlin-classes/${variant.name}") {
                exclude(coverageExclusions)
            }
            val javaClasses = fileTree("${buildDir}/intermediates/javac/${variant.name}") {
                exclude(coverageExclusions)
            }
            classDirectories.setFrom(mainClasses.plus(javaClasses))

            val sourceDirs = fileTree("$projectDir/src/main/java",)

            sourceDirectories.setFrom(sourceDirs)
            executionData.setFrom(file(testJacocoExecPath))
        }

        jacocoTestReport.dependsOn(reportTask)

    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            isIncludeNoLocationClasses = true
            // Required for JDK 11 with the above
            // https://github.com/gradle/gradle/issues/5184#issuecomment-391982009
            excludes = listOf("jdk.internal.*")
        }
    }
}



