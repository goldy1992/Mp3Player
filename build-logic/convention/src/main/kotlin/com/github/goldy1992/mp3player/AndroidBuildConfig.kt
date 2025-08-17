package com.github.goldy1992.mp3player

import com.android.build.api.dsl.LibraryExtension

internal fun configureBuildConfig(
    libraryExtension: LibraryExtension,
) {
    libraryExtension.apply {
       this.buildFeatures.apply {
           buildConfig = true
       }
    }

}