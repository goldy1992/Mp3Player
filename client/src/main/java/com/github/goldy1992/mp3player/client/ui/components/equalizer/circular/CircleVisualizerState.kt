package com.github.goldy1992.mp3player.client.ui.components.equalizer.circular

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.IntSize
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.Fountain
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.CircularCurveFitter
import com.github.goldy1992.mp3player.client.utils.visualizer.circle.cyclic.PolikarpotchkinCurveFitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty


@Composable
fun rememberCircleVisualizerState(
    scope : CoroutineScope = rememberCoroutineScope(),
    insetPx: Float
): CircleVisualizerState = remember(scope, insetPx) {
    CircleVisualizerState(scope, insetPx)
}


@Stable
class CircleVisualizerState(
    val scope: CoroutineScope,
    val insetPx : Float
){

    private val _bezierCurveState = MutableStateFlow<List<CubicBezierCurveOffset>>(emptyList())
    private var circleFitter =  PolikarpotchkinCurveFitter(IntSize.Zero)
    val bezierCurveState : StateFlow<List<CubicBezierCurveOffset>> = _bezierCurveState

    fun setSize(size: IntSize) {
        circleFitter = PolikarpotchkinCurveFitter(size, insetPx)
    }

    fun setAnimatedFrequencies(animatedFrequencies : List<Float>) {
       scope.launch {
           val beziers = circleFitter.generateBeziers(animatedFrequencies)
           _bezierCurveState.value = beziers
       }
    }
}

