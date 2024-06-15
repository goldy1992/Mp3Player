package com.github.goldy1992.mp3player.client.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SharedElementComposable(
    animationLabel: String = "TestAnimation",
    content: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Unit = {}
) {
    SharedTransitionLayout {
        AnimatedContent(
            targetState = true,
            label = animationLabel
        ) {
            content(this)
        }
    }
}