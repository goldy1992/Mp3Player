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

    // hilt
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)

}

sonarqube {
    this.isSkipProject = true
}