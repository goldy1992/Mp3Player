package com.github.goldy1992.mp3player

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureBuildConfig(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
       this.buildFeatures.apply {
           buildConfig = true
       }
    }

}