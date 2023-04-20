plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}
//apply from: rootProject.file("jacoco-with-test-support.gradle")

android {
    compileSdkVersion = libs.versions.compileSdk
    //buildToolsVersion build_tools_version
    testBuildType = "debug"

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += ['**/attach_hotspot_windows.dll', 'META-INF/LICENSE.md', 'META-INF/LICENSE-notice.md', 'META-INF/AL2.0', 'META-INF/ASM', 'META-INF/licenses/ASM', 'META-INF/LGPL2.1']
        }
    }


    defaultConfig {
        minSdkVersion = libs.versions.minSdk
        targetSdkVersion = libs.versions.targetSdk
        testInstrumentationRunner = "com.github.goldy1992.mp3player.client.CustomTestRunner"
        /*makes the Android Test Orchestrator run its "pm clear" command after each test invocation.
        Ensures app's state is completely cleared between tests. */
        //testInstrumentationRunnerArguments clearPackageData: 'true'
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        create("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        debug {
            isDefault = true
            isTestCoverageEnabled = true
            enableUnitTestCoverage =  true
            isMinifyEnabled = false
        }

    }
    flavorDimensions flavor_dimen_mode
    productFlavors {
        full {
            isDefault = true
            dimension = flavor_dimen_mode
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

        unitTests {
            includeAndroidResources = true
            returnDefaultValues = true
        }
        unitTests.all {
            testLogging {
                events = listOf("passed", "skipped", "failed", "standardOut", "standardError")
                outputs.upToDateWhen {false}
                showStandardStreams = false
            }
            jacoco { // FOR USE IN TEST EXECUTION
                includeNoLocationClasses = true
                includes = ['com/github/goldy1992/mp3player/client/**']
                excludes = EXCLUDE_LIST
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
    implementation(libs.kotlin.androidx.annotation)
    implementation(libs.kotlin.androidx.appcompat)

    implementation(libs.kotlin.androidx.core.kotlin)
    implementation(libs.kotlin.androidx.core.kotlin)
    implementation(libs.kotlin.androidx.media3.session)
    implementation(libs.kotlin.apache.commons.io)
    implementation(libs.kotlin.apache.commons.collections4)
    implementation(libs.kotlin.apache.commons.lang3)
    implementation(libs.kotlin.apache.commons.math3)

    implementation(libs.kotlin.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.kotlin.androidx.lifecycle.viewmodel.kotlin)
    implementation(libs.kotlin.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlin.androidx.concurrent.futures.kotlin)
    implementation(libs.kotlin.androidx.activity.kotlin)
    androidTestImplementation(project(":client"))

    // compose
    implementation(libs.kotlin.androidx.compose.runtime)
    implementation(libs.kotlin.androidx.compose.ui)
    implementation(libs.kotlin.androidx.compose.foundation)
    implementation(libs.kotlin.androidx.compose.foundation.layout)
    implementation(libs.kotlin.androidx.compose.material)
    implementation(libs.kotlin.androidx.compose.material3)
    implementation(libs.kotlin.androidx.compose.material3.windowsizes)
    implementation(libs.kotlin.androidx.compose.material.icons.core)
    implementation(libs.kotlin.androidx.compose.ui.preview)
    implementation(libs.kotlin.androidx.compose.material.icons.extended)
    implementation(libs.kotlin.androidx.compose.activity)
    debugImplementation(libs.kotlin.androidx.compose.ui.tooling)

    implementation group: 'com.google.dagger', name: 'hilt-android', version: hilt_version
    implementation group: 'androidx.hilt', name: 'hilt-navigation-compose', version: '1.0.0'

    def accompanist_version = '0.30.1'
    implementation group: "com.google.accompanist", name: "accompanist-pager", version: accompanist_version
    implementation group: "com.google.accompanist", name: "accompanist-insets", version: accompanist_version
    implementation group: "com.google.accompanist", name: "accompanist-pager-indicators", version: accompanist_version
    implementation group: "com.google.accompanist", name: "accompanist-navigation-animation", version: accompanist_version
    implementation group: "io.coil-kt", name: "coil-compose", version: "2.2.2"

    implementation "androidx.window:window:1.0.0-beta04"
    implementation group: "androidx.datastore", name: "datastore-preferences", version: datastore_preferences_version


    // Compose testing dependencies
    androidTestImplementation "androidx.compose.ui:ui-test"
    androidTestImplementation "androidx.compose.ui:ui-test-junit4"
    debugImplementation "androidx.compose.ui:ui-test-manifest"

    // UI Tests
    // Test rules and transitive dependencies:

    testImplementation("androidx.compose.ui:ui-test-junit4:$compose_test_version")
    // Needed for createComposeRule, but not createAndroidComposeRule:



    // hilt
    kapt group: 'com.google.dagger', name: 'hilt-android-compiler', version: hilt_version
    kapt group: 'com.google.dagger', name: 'hilt-compiler', version: hilt_version
    kaptTest group: 'com.google.dagger', name: 'hilt-android-compiler', version: hilt_version
    kaptTest group: 'com.google.dagger', name: 'hilt-compiler', version: hilt_version

    androidTestImplementation group: 'com.google.dagger', name: 'hilt-android-testing', version: hilt_version
    kaptAndroidTest group: 'com.google.dagger', name: 'hilt-android-compiler', version: hilt_version
    kaptAndroidTest group: 'com.google.dagger', name: 'hilt-compiler', version: hilt_version

    androidTestImplementation project(':commons')

    //  androidTestImplementation group: 'org.jetbrains.kotlin', name: 'kotlin-stdlib-jdk8', version: kotlin_version
    //androidTestImplementation junitUnitTests
//    androidTestImplementation group: 'androidx.test', name: 'rules', version: test_rules_version
//    androidTestImplementation group: 'androidx.test.espresso', name: 'espresso-core', version: espresso_core_version
//    androidTestImplementation group: 'androidx.test.espresso', name: 'espresso-core', version: espresso_core_version
//    androidTestImplementation group: 'androidx.test.espresso', name: 'espresso-contrib', version: espresso_core_version
//    androidTestImplementation group: 'androidx.test', name: 'core-ktx', version: test_core_version
//    androidTestImplementation group: 'androidx.test.espresso',  name: 'espresso-intents', version: '3.4.0'

//    androidTestImplementation group: 'androidx.test.ext', name: 'junit', version: junit_ext_version
//    androidTestImplementation group: 'androidx.test', name: 'runner', version: test_runner_version
//    androidTestUtil group: "androidx.test", name: "orchestrator", version: test_orchestrator_version
//    androidTestImplementation group: 'androidx.test', name: 'monitor', version: monitor_version
    androidTestImplementation group: "org.mockito.kotlin", name: "mockito-kotlin", version: mockito_kotlin_version
    //androidTestImplementation group: 'com.linkedin.dexmaker', name: 'dexmaker-mockito-inline-extended', version: '2.28.3'
//    androidTestImplementation group: 'org.jetbrains.kotlin', name: 'kotlin-reflect', version: kotlin_version
//    androidTestImplementation 'androidx.arch.core:core-common:2.1.0'
//    androidTestImplementation 'androidx.arch.core:core-runtime:2.1.0'
//    androidTestImplementation 'androidx.arch.core:core-testing:2.1.0'
//    androidTestImplementation group: 'androidx.test.espresso', name: 'espresso-idling-resource', version: espresso_core_version
//    androidTestImplementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    // Test rules and transitive dependencies:
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
//    androidTestImplementation group: 'androidx.test.uiautomator', name: 'uiautomator', version: ui_automator_version

//    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")
//    debugImplementation group: 'androidx.core', name: 'core-ktx', version: androidx_core_ktx_version
//    debugImplementation group: 'androidx.activity', name: 'activity-ktx', version: activity_ktx_version
//    debugImplementation group: 'androidx.test.espresso', name: 'espresso-idling-resource', version: espresso_core_version

    //testImplementation group: 'org.jetbrains.kotlin', name: 'kotlin-stdlib-jdk8', version: kotlin_version
    testImplementation("org.robolectric:robolectric:$robolectric_version") {
        exclude group: "com.google.auto.service", module: "auto-service"
    }
    testImplementation group: 'androidx.test', name: 'core-ktx', version: test_core_version
    testImplementation group: "org.mockito.kotlin", name: "mockito-kotlin", version: mockito_kotlin_version
    testImplementation group: 'org.mockito', name: 'mockito-inline', version: mockito_inline_version
    testImplementation group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-test', version: coroutines_version
    testImplementation junitUnitTests
            testImplementation group: 'com.google.dagger', name: 'hilt-android', version: hilt_version
    testImplementation group: 'com.google.dagger', name: 'hilt-android-testing', version: hilt_version
    testImplementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
}

kapt {
    correctErrorTypes true
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