import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("mp3player.android.application.variant_filter")
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

    defaultConfig {
        applicationId = "com.github.goldy1992.mp3player"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = providers.gradleProperty("version.code").get().toInt()
        versionName = providers.gradleProperty("version.name").get()
        testInstrumentationRunner = "com.github.goldy1992.mp3player.Mp3PlayerHiltTestRunner"
        missingDimensionStrategy("fullUnittest", "fullDebug")
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
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

//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }

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

    // hilt
    implementation(libs.hilt.android.core)
    ksp(libs.hilt.compiler)

    val composeBom = platform(libs.androidx.compose.bom)

    "androidTestAutomationImplementation"(composeBom)

    "androidTestAutomationImplementation"(libs.hilt.android.core)
    "kspAndroidTestAutomation"(libs.hilt.compiler)
    "androidTestAutomationImplementation"(libs.hilt.android.testing)

    "androidTestAutomationImplementation"(libs.androidx.test.core)
    "androidTestAutomationImplementation"(libs.androidx.test.ext.junit)
    "androidTestAutomationImplementation"(libs.androidx.uiautomator)
    "androidTestAutomationImplementation"(libs.androidx.test.rules)
    "androidTestAutomationImplementation"(libs.androidx.compose.ui.test)
    "androidTestAutomationImplementation"(libs.androidx.compose.ui.test.junit4)

}

sonarqube {
    this.isSkipProject = true
}