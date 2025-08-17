plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("mp3player.android.room")
    id("mp3player.android.library.jacoco")
    id("mp3player.android.library.variant_filter")
    id("mp3player.android.library.buildconfig")
//    id("mp3player.java.toolchain")
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
    }

    lint {
        disable.add("UnsafeOptInUsageError")
    }

//    kotlinOptions {
//        jvmTarget = "17"
//        freeCompilerArgs += listOf(
//            "-Xcontext-receivers",
//            "-opt-in=androidx.media3.common.util.UnstableApi"
//        )
//    }
    namespace = "com.github.goldy1992.mp3player.service"

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
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }


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
    //kspTest(libs.hilt.compiler)
}



sonarqube {
    setAndroidVariant("fullDebug")
    properties {
        property("sonar.java.binaries", "${project.layout.buildDirectory}/intermediates/javac/fullDebug/classes, ${project.layout.buildDirectory}/tmp/kotlin-classes/fullDebug")
        property("sonar.java.test.binaries", "${project.layout.buildDirectory}/intermediates/javac/fullDebugUnitTest/classes, ${project.layout.buildDirectory}/tmp/kotlin-classes/fullDebugUnitTest")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.layout.buildDirectory}/reports/jacoco/jacocoTestFullDebugUnitTestReport/jacocoTestFullDebugUnitTestReport.xml")
        property("sonar.junit.reportPaths", "${project.layout.buildDirectory}/test-results/testFullDebugUnitTest/TEST-*.xml")
        property("sonar.androidLint.reportPaths", "${project.layout.buildDirectory}/reports/lint-results-fullDebug.xml")
    }
}
