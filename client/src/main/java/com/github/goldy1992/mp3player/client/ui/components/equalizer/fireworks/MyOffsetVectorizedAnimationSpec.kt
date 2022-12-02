package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks

import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorizedAnimationSpec
import androidx.compose.animation.core.VectorizedFiniteAnimationSpec
import androidx.compose.ui.geometry.Offset

class MyOffsetVectorizedAnimationSpec(override val isInfinite: Boolean) :
    VectorizedFiniteAnimationSpec<AnimationVector2D>{

    override fun getDurationNanos(initialValue: AnimationVector2D,
                                  targetValue: AnimationVector2D,
                                  initialVelocity: AnimationVector2D): Long {
        return 0L
    }

    override fun getValueFromNanos(
        playTimeNanos: Long,
        initialValue: AnimationVector2D,
        targetValue: AnimationVector2D,
        initialVelocity: AnimationVector2D
    ): AnimationVector2D {
        return AnimationVector2D(0f, 0f)
    }

    override fun getVelocityFromNanos(
        playTimeNanos: Long,
        initialValue: AnimationVector2D,
        targetValue: AnimationVector2D,
        initialVelocity: AnimationVector2D
    ): AnimationVector2D {
        return AnimationVector2D(0f, 0f)
    }


}
