import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

group = "com.github.goldy1992.mp3player.buildlogic"

// Configure the build-logic plugins to target JDK 21
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions{
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(libs.androidx.room.gradle.plugin)
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
        register("androidRoom") {
            id = "mp3player.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidLibraryBuildConfig") {
            id = "mp3player.android.library.buildconfig"
            implementationClass = "AndroidLibraryBuildConfigConventionPlugin"
        }
        register("javaToolchainConventionPlugin") {
            id = "mp3player.java.toolchain"
            implementationClass = "JavaToolchainConventionPlugin"
        }
        register("testConventionPlugin") {
            id = "mp3player.android.test"
            implementationClass = "TestConventionPlugin"
        }
    }
}