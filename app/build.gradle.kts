plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}

android {
    testBuildType = "debug"
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
    compileSdk = libs.versions.compileSdk.get().toInt()
    //buildToolsVersion build_tools_version

    defaultConfig {
        applicationId = "com.github.goldy1992.mp3player"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = providers.gradleProperty("version.code").get().toInt()
        versionName = providers.gradleProperty("version.name").get()
        testInstrumentationRunner = "com.github.goldy1992.mp3player.Mp3PlayerHiltTestRunner"
        missingDimensionStrategy("fullUnittest", "fullDebug")
    }

    signingConfigs {
        create("release") {
            storeFile = file(providers.gradleProperty("full_release_storeFile").get())
            storePassword = providers.gradleProperty("full_release_storePassword").get()
            keyAlias = providers.gradleProperty("full_release_keyAlias").get()
            keyPassword = providers.gradleProperty("full_release_keyPassword").get()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isDefault = true
            isMinifyEnabled = false
            isDebuggable = true
            enableUnitTestCoverage = false
        }
    }
    flavorDimensions += "mode"
    productFlavors {
        create("full") {
            isDefault = true
            applicationIdSuffix = ".full"
            versionNameSuffix = "-full"
            resValue("string", "app_name", "Music Player")
        }
        create("automation") {
            applicationIdSuffix = ".automation"
            versionNameSuffix = "-automation"
            resValue("string", "app_name", "Test Music Player")
            matchingFallbacks += listOf("full")
        }
    }

//    variantFilter { variant ->
//        var names = variant.flavors.name
//
//
//        if (names.contains("automation")) {
//            if (variant.buildType.name == "release") {
//                setIgnore(true)
//            }
//        }
//
//    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }
    lint {
        abortOnError = false
        disable += "GoogleAppIndexingWarning" + "PrivateResource"
    }
    namespace = "com.github.goldy1992.mp3player"
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // local libs
    implementation(project(":commons"))
    implementation(project(":service"))
    implementation(project(":client"))

//    implementation group: 'androidx.activity', name: 'activity-ktx', version: activity_ktx_version
//    implementation 'androidx.core:core-ktx:+'
//    debugImplementation group: 'androidx.core', name: 'core-ktx', version: androidx_core_ktx_version
    // hilt
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)
//    androidTestImplementation group: 'com.google.dagger', name: 'hilt-android', version: hilt_version
//    androidTestImplementation group: 'com.google.dagger', name: 'hilt-android-testing', version: hilt_version
//    kaptAndroidTest group: 'com.google.dagger', name: 'hilt-android-compiler', version: hilt_version
    //   androidTestImplementation group: 'androidx.hilt', name: 'hilt-lifecycle-viewmodel', version: HILT_JETPACK_VERSION

//    kaptAndroidTest group: 'com.google.dagger', name: 'hilt-compiler', version: hilt_version


//    androidTestImplementation junitUnitTests
//
//    androidTestImplementation group: 'androidx.test', name: 'core', version: test_core_version
//    androidTestImplementation group: 'androidx.annotation', name: 'annotation', version: annotation_version
//    androidTestImplementation group: 'androidx.test.ext', name: 'junit', version: junit_ext_version
//    androidTestImplementation group: 'androidx.test', name: 'rules', version: test_rules_version
//    androidTestImplementation group: 'androidx.test', name: 'runner', version: test_runner_version
//    androidTestImplementation group: 'androidx.test', name: 'monitor', version: monitor_version
//    androidTestImplementation group: 'androidx.test.espresso', name: 'espresso-contrib', version: espresso_core_version
//
//
//    androidTestImplementation(group: 'androidx.test.espresso', name: 'espresso-core', version: espresso_core_version) {
//        exclude module: "support-annotations"
//    }
//    androidTestImplementation group: 'androidx.test.uiautomator', name: 'uiautomator', version: ui_automator_version
//
//    // UI Tests
//    androidTestImplementation group: 'androidx.compose.ui', name: 'ui-test-junit4', version: compose_version
//    // Test rules and transitive dependencies:
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")

}

//sonarqube {
//    skipProject = true
//}