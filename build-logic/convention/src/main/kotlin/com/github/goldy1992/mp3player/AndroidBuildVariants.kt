package com.github.goldy1992.mp3player

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project

internal fun Project.configureBuildVariants(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    androidComponentsExtension.apply {
        beforeVariants { variant ->
            val debugPrefix = "projectName: ${this@configureBuildVariants.project.name}, variant name: ${variant.name}"
            logger.trace("configuring $debugPrefix")
            if (variant.name.contains("automation")) {
                logger.debug("$debugPrefix contains automation")
                if (variant.buildType == "release") {
                    logger.debug("$debugPrefix contains release")
                    variant.enable = false
                }
            }
        }

    }

}