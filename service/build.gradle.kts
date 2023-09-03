plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("mp3player.android.library.jacoco")
    id("mp3player.android.library.variant_filter")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "com.github.goldy1992.mp3player.client.CustomTestRunner"
        /*makes the Android Test Orchestrator run its "pm clear" command after each test invocation.
        Ensures app's state is completely cleared between tests. */
        //testInstrumentationRunnerArguments clearPackageData: 'true'
        consumerProguardFiles("consumer-rules.pro")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isDefault = true
            enableUnitTestCoverage = true
            isMinifyEnabled = false
        }
    }
    flavorDimensions += "mode"
            productFlavors {
                create("full") {
                    isDefault = true
                    dimension = "mode"
                }
                create("automation") {
                    dimension = "mode"
                }
            }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    namespace  = "com.github.goldy1992.mp3player.service"
}

dependencies {

    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.apache.commons.collections4)
    implementation(libs.apache.commons.lang3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.kotlin)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(project(":commons"))

    implementation(libs.androidx.room.kotlin)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.robolectric) {
        exclude(group = "com.google.auto.service", module = "auto-service")
    }

    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.androidx.test.core.kotlin)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.junit4)

    // hilt
    implementation(libs.hilt.android.core)
    ksp(libs.hilt.compiler)
    testImplementation(libs.hilt.android.core)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)
}


sonarqube {
    androidVariant = "fullDebug"
    properties {
        property("sonar.java.binaries", "${project.buildDir}/intermediates/javac/fullDebug/classes, ${project.buildDir}/tmp/kotlin-classes/fullDebug")
        property("sonar.java.test.binaries", "${project.buildDir}/intermediates/javac/fullDebugUnitTest/classes, ${project.buildDir}/tmp/kotlin-classes/fullDebugUnitTest")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/jacocoTestFullDebugUnitTestReport/jacocoTestFullDebugUnitTestReport.xml")
        property("sonar.junit.reportPaths", "${project.buildDir}/test-results/testFullDebugUnitTest/TEST-*.xml")
        property("sonar.androidLint.reportPaths", "${buildDir}/reports/lint-results-fullDebug.xml")
    }
}
