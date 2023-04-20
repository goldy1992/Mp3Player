import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}
//apply from: rootProject.file("jacoco-with-test-support.gradle")

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    //buildToolsVersion build_tools_version
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
        testInstrumentationRunner = "com.github.goldy1992.mp3player.client.CustomTestRunner"
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
        //      execution 'ANDROIDX_TEST_ORCHESTRATOR'
        animationsDisabled = true
    }
    testOptions.unitTests {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true

        all { test ->
            with(test) {
                testLogging {
                    outputs.upToDateWhen {false}
                    showStandardStreams = false
                    events = setOf(
                        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                    )
                }

                jacoco { // FOR USE IN TEST EXECUTION
                //includeNoLocationClasses = true
               // includes = ['com/github/goldy1992/mp3player/client/**']
               // excludes = EXCLUDE_LIST
            }
            }
        }
    }

//    variantFilter { variant ->
//        def names = variant.flavors*.name
//
//        if (names.contains("automation")) {
//            if (variant.buildType.name == "release" || variant.buildType.name == "debug") {
//                setIgnore(true)
//            }
//        }
//    }
    namespace = "com.github.goldy1992.mp3player.client"

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // local libs
    implementation(project(":commons"))

    // Import the Compose BOM
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    androidTestImplementation(composeBom)

    implementation(libs.kotlin.stdlib)
    androidTestImplementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.core.kotlin)
    implementation(libs.androidx.media3.session)
    implementation(libs.apache.commons.io)
    implementation(libs.apache.commons.collections4)
    implementation(libs.apache.commons.lang3)
    implementation(libs.apache.commons.math3)

    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewmodel.kotlin)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.concurrent.futures.kotlin)
    implementation(libs.androidx.activity.kotlin)
    androidTestImplementation(project(":client"))

    // compose
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowsizes)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui.preview)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.hilt.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // hilt
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)


    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.coil.compose)
    implementation(libs.androidx.window)
    implementation(libs.androidx.datastore.preferences)



    // Compose testing dependencies
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // UI Tests
    // Test rules and transitive dependencies:
    testImplementation(libs.androidx.compose.ui.test.junit4)

      // Needed for createComposeRule, but not createAndroidComposeRule:



    // hilt
    testImplementation(libs.hilt.android.core)
    kaptTest(libs.hilt.compiler)


    androidTestImplementation(libs.hilt.android.core)
    kaptAndroidTest(libs.hilt.compiler)

    androidTestImplementation(project(":commons"))
    androidTestImplementation(libs.mockito.kotlin)

    // Test rules and transitive dependencies:

    //testImplementation group: 'org.jetbrains.kotlin', name: 'kotlin-stdlib-jdk8', version: kotlin_version
    testImplementation(libs.robolectric) {
        exclude(group = "com.google.auto.service", module = "auto-service")
    }

    testImplementation(libs.androidx.test.core.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.androidx.lifecycle.viewmodel.kotlin)
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