package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable


@Composable
fun SharedElementComposable(
    animationLabel: String = "TestAnimation",
    content: @Composable SharedTransitionScope.() -> Unit = {}
) {
    SharedTransitionLayout {

        AnimatedContent(
            targetState = true,
            label = animationLabel
        ) { v -> v
            content()
        }
    }
}