package com.github.goldy1992.mp3player.client.ui.components.equalizer.cards

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.components.equalizer.smoothline.SmoothLineEqualizerCanvas
import kotlinx.coroutines.CoroutineScope

//@Preview
//@Composable
//fun SmoothLineCard(
//    modifier : Modifier = Modifier,
//    density: Density = LocalDensity.current,
//    initialCanvasSizeDpPx: DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, density),
//    initialPaddingDpPx : DpPxSize = DpPxSize.createDpPxSizeFromDp(10.dp, 10.dp, density),
//    frequencyPhases : () -> List<Float> = {  listOf(100f, 200f, 300f, 150f) },
//    scope: CoroutineScope = rememberCoroutineScope()
//) {
//    var size = initialCanvasSizeDpPx
//    val padding = initialPaddingDpPx
//
//    Card(border = BorderStroke(2.dp, Color.Red),
//        modifier = modifier
//            .width(size.widthDp)
//            .height(size.heightDp)
//        ,
//        elevation = CardDefaults.outlinedCardElevation()) {
//        var equalizerSize by remember { mutableStateOf(DpPxSize.createDpPxSizeFromPx(size.widthPx - (padding.widthPx*2), (size.heightPx - (padding.widthPx*2)) * (5f/7f), density)) }
//        Row(
//            Modifier
//                .padding(horizontal = padding.widthDp)
//                .weight(8f)
//                .onSizeChanged {
//                    equalizerSize = DpPxSize.createDpPxSizeFromPx(
//                        it.width.toFloat(),
//                        it.height.toFloat(),
//                        density
//                    )
//                }) {
//            SmoothLineEqualizer(
//                frequencyPhasesState = frequencyPhases,
//                waveAmplitude = 0.25f,
//                canvasDpPxSize = equalizerSize,
//                scope = scope
//            )
//        }
//        Column(
//            Modifier
//                .weight(2f)
//                .padding(horizontal = padding.widthDp),
//            verticalArrangement = Arrangement.Center) {
//            Text("Smooth Line")
//        }
//    }
//}

@Preview
@Composable
fun SmoothLineCard(
    modifier : Modifier = Modifier,
    frequencyPhases : () -> List<Float> = {  listOf(100f, 200f, 300f, 150f) },
    scope: CoroutineScope = rememberCoroutineScope()
) {
    EqualizerCard(
        modifier = modifier,
        title = "Smooth Line",) { canvasSize, containerModifier ->
        SmoothLineEqualizerCanvas(
            frequencyPhasesState = frequencyPhases,
            canvasSize = canvasSize,
            modifier = containerModifier)
    }

}

