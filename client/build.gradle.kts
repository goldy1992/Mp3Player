import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("mp3player.android.library.jacoco")
    id("mp3player.android.library.variant_filter")
    id("mp3player.android.library.buildconfig")
    id("mp3player.android.test")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    testBuildType = "debug"

    buildFeatures {
        compose = true
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
        /*makes the Android Test Orchestrator run its "pm clear" command after each test invocation.
        Ensures app's state is completely cleared between tests. */
        // testInstrumentationRunnerArguments clearPackageData: 'true'
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
            enableAndroidTestCoverage = true
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

    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.addAll(
                 listOf(
                    // Enable experimental  APIs
                    "-opt-in=androidx.compose.animation.core.ExperimentalAnimationSpecApi",
                    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                    "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                    "-opt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi",
                    "-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi",
                    "-opt-in=androidx.media3.common.util.UnstableApi",
                    "-opt-in=coil.annotation.ExperimentalCoilApi",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xcontext-receivers"
                )
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

    }


    testOptions {
        animationsDisabled = false
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    lint {
        disable.add("UnsafeOptInUsageError")
    }

    namespace = "com.github.goldy1992.mp3player.client"
}

dependencies {
    implementation(libs.androidx.core.kotlin)
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(composeBom)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
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
    implementation(libs.androidx.compose.animation.beta)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowsizes)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.preview)
    implementation(libs.androidx.concurrent.futures.kotlin)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.lifecycle.viewmodel.kotlin)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.google.play.review)
    implementation(libs.google.play.review.ktx)
    implementation(libs.hilt.android.core)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(project(":commons"))

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    ksp(libs.hilt.compiler)

    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.coil.test)
    androidTestImplementation(libs.kotlin.stdlib)
    androidTestImplementation(project(":commons"))

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