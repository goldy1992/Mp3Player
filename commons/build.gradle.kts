plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    id("mp3player.android.library.jacoco")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            isMinifyEnabled = false
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
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

    namespace = "com.github.goldy1992.mp3player.commons"

}

dependencies {

    implementation(libs.apache.commons.lang3)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.core.kotlin)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)


    testImplementation(libs.robolectric) {
        exclude(group = "com.google.auto.service", module = "auto-service")
    }
    testImplementation(libs.junit4)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
}

sonarqube {
    androidVariant = "debug"
    properties {
        property("sonar.java.binaries", "${project.buildDir}/intermediates/javac/debug/classes,${project.buildDir}/tmp/kotlin-classes/debug")
        property("sonar.coverage.jacoco.xmlReportPaths", "${project.buildDir}/reports/coverage/test/debug/report.xml")
        property("sonar.junit.reportPaths", "${project.buildDir}/test-results/testDebugUnitTest/TEST-*.xml")
    }
}
