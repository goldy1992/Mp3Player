plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    testBuildType = "debug"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    packaging {
        resources {
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/ASM"
            excludes += "META-INF/licenses/ASM"
            excludes += "META-INF/LGPL2.1"
        }
    }


    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //testInstrumentationRunner = "com.github.goldy1992.mp3player.client.CustomTestRunner"
        /*makes the Android Test Orchestrator run its "pm clear" command after each test invocation.
        Ensures app's state is completely cleared between tests. */
        //testInstrumentationRunnerArguments clearPackageData: 'true'
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isDefault = true
            enableUnitTestCoverage =  true
            isMinifyEnabled = false
        }

    }
    flavorDimensions += "mode"
    productFlavors {
        create("full") {
            isDefault = true
            dimension = "mode"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true

            all { test ->
                with(test) {
                    extensions.configure(JacocoTaskExtension::class) {
                        this.isIncludeNoLocationClasses = true
                    }

                    testLogging {
                        outputs.upToDateWhen { false }
                        showStandardStreams = false
                        events = setOf(
                            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                        )
                    }
                }
            }
        }
    }
    namespace = "com.github.goldy1992.mp3player.client"

}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(composeBom)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.kotlin)
    implementation(libs.androidx.media3.session)
    implementation(libs.apache.commons.io)
    implementation(libs.apache.commons.collections4)
    implementation(libs.apache.commons.lang3)
    implementation(libs.apache.commons.math3)
    implementation(libs.androidx.activity.kotlin)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowsizes)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.preview)
    implementation(libs.androidx.concurrent.futures.kotlin)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewmodel.kotlin)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.window)
    implementation(libs.coil.compose)
    implementation(libs.hilt.android.core)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(project(":commons"))

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    kapt(libs.hilt.compiler)
    kaptTest(libs.hilt.compiler)

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlin.stdlib)
    androidTestImplementation(project(":commons"))

    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.lifecycle.viewmodel.kotlin)
    testImplementation(libs.androidx.test.core.kotlin)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric) {
        exclude(group = "com.google.auto.service", module = "auto-service")
    }

}

kapt {
    correctErrorTypes = true
}

//sonarqube {
//    androidVariant 'fullDebug'
//    properties {
//        property 'sonar.java.binaries', "${project.buildDir}/intermediates/javac/fullDebug/classes, ${project.buildDir}/tmp/kotlin-classes/fullDebug"
//        property 'sonar.java.test.binaries', "${project.buildDir}/intermediates/javac/fullDebugUnitTest/classes, ${project.buildDir}/tmp/kotlin-classes/fullDebugUnitTest"
//        property 'sonar.coverage.jacoco.xmlReportPaths', "${project.buildDir}/reports/coverage/test/full/debug/report.xml"
//        property 'sonar.junit.reportPaths', "${project.buildDir}/test-results/testFullDebugUnitTest/TEST-*.xml"
//
//    }
//}