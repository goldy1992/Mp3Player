plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}
//apply from: rootProject.file("jacoco-with-test-support.gradle")

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

//    variantFilter { variant ->
//        def names = variant.flavors*.name
//
//        if (names.contains("automation")) {
//            if (variant.buildType.name == "release" || variant.buildType.name == "unittest") {
//                setIgnore(true)
//            }
//        }
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

//    testOptions {
//        animationsDisabled true
//
//        unitTests {
//            includeAndroidResources = true
//            returnDefaultValues = true
//        }
//        unitTests.all {
//            testLogging {
//                events "passed", "skipped"//, "failed", "standardOut", "standardError"
//                outputs.upToDateWhen { false }
//                showStandardStreams = false
//            }
//            jacoco {
//                includeNoLocationClasses = true
//                includes = ['com/github/goldy1992/mp3player/service/**']
//                excludes =  EXCLUDE_LIST
//            }
//        }
//    }
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

    // hilt
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)
    testImplementation(libs.hilt.android.core)
    kaptTest(libs.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}

//sonarqube {
//    androidVariant 'fullDebug'
//    properties {
//        property 'sonar.java.binaries', "${project.buildDir}/intermediates/javac/fullDebug/classes"
//        property 'sonar.java.test.binaries', "${project.buildDir}/intermediates/javac/fullDebugUnitTest/classes, ${project.buildDir}/tmp/kotlin-classes/fullDebugUnitTest"
//        property 'sonar.coverage.jacoco.xmlReportPaths', "${project.buildDir}/reports/coverage/test/full/debug/report.xml"
//        property 'sonar.junit.reportPaths', "${project.buildDir}/test-results/testFullDebugUnitTest/TEST-*.xml"
//    }
//}

