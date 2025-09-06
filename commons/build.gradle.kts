plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    id("mp3player.android.library.jacoco")
    id("mp3player.android.library.buildconfig")
    id("mp3player.android.library.variant_filter")
//    id("mp3player.java.toolchain")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    namespace = "com.github.goldy1992.mp3player.commons"

}
tasks.withType<Test> {
    failOnNoDiscoveredTests = false
}
dependencies {

    implementation(libs.apache.commons.lang3)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.core.kotlin)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android.core)
    ksp(libs.hilt.compiler)


    testImplementation(libs.robolectric) {
   //     exclude(group = "com.google.auto.service", module = "auto-service")
    }
    testImplementation(libs.junit4)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
}

sonarqube {
    setAndroidVariant("debug")
    properties {
        property("sonar.java.binaries", "${project.layout.buildDirectory}/intermediates/javac/debug/classes,${project.layout.buildDirectory}/tmp/kotlin-classes/debug")
        property("sonar.java.test.binaries", "${project.layout.buildDirectory}/intermediates/javac/debugUnitTest/classes, ${project.layout.buildDirectory}/tmp/kotlin-classes/debugUnitTest")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.layout.buildDirectory}/reports/jacoco/jacocoTestDebugUnitTestReport/jacocoTestDebugUnitTestReport.xml")
        property("sonar.junit.reportPaths", "${project.layout.buildDirectory}/test-results/testDebugUnitTest/TEST-*.xml")
        property("sonar.androidLint.reportPaths", "${project.layout.buildDirectory}/reports/lint-results-debug.xml")
    }
}
