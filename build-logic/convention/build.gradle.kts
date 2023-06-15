import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.github.goldy1992.mp3player.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibraryJacoco") {
            id = "mp3player.android.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }
        register("androidLibraryVariantFilter") {
            id = "mp3player.android.library.variant_filter"
            implementationClass = "AndroidLibraryVariantFilterConvention"
        }
        register("androidApplicationVariantFilter") {
            id = "mp3player.android.application.variant_filter"
            implementationClass = "AndroidApplicationVariantFilterConvention"
        }
    }
}