package com.github.goldy1992.mp3player.client.ui.components.equalizer

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.screens.DpPxSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val logTag = "EqualizerContainer"

@Composable
fun EqualizerContainer(
    modifier: Modifier = Modifier,
    frequencyValues : () -> List<Float> =  {listOf(100f, 200f, 300f, 150f)},
    scope : CoroutineScope = rememberCoroutineScope(),
    containerSize: DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
    equalizer :  @Composable (
        frequencyValues : () -> List<Float>,
        canvasSize : DpPxSize,
        modifier: Modifier) -> Unit) {

    //Log.i(logTag, "canvasSize: $containerSize")
    val frequencyPhases = frequencyValues()
    val frequencyAnimatableList : SnapshotStateList<Animatable<Float, AnimationVector1D>> =
        remember(frequencyPhases.size) {
            mutableStateListOf<Animatable<Float, AnimationVector1D>>().apply {
                Log.i(logTag, "retrigger remember")
                for (i in frequencyPhases) add(Animatable(i))
            }
    }
    LaunchedEffect(frequencyPhases) {
    //    Log.i(logTag, "Triggered launch effect: frequencyPhases $frequencyPhases")
        for (i in frequencyPhases.indices) {
            this.launch { frequencyAnimatableList[i].animateTo(targetValue = frequencyPhases[i], animationSpec = tween(300)) }
        }
    }

    equalizer(canvasSize = containerSize,
                frequencyValues = {frequencyAnimatableList.map { it.value }.toList()},
                modifier = modifier)


}