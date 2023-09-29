package com.github.goldy1992.mp3player.client.ui.screens.library

//import androidx.compose.animation.Animatable
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun MyAnim() {
    var left by remember { mutableStateOf(true) }

    MyAnimatedLayout(Modifier.fillMaxSize(),
        leftSide= left
    ) {
        Button(
            onClick = { left = !left },) {
            Text("animated")
        }
    }
}



@Composable
@Preview
fun MyAnimatedLayout(
    modifier : Modifier = Modifier,
    leftSide: Boolean = true,
    scope : CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit = {}) {
    val x = remember {  Animatable(0f) }

   // val color = remember { Animatable(Color.Gray) }

    Layout(modifier = modifier,
        content = content) { measurables, constraints: Constraints ->

        val placeables = measurables.map { measurable ->
            // Measure each children
            val p = measurable.measure(Constraints())
            p
        }


        // Set the size of the layout as big as it can
        layout(constraints.minWidth, constraints.maxHeight) {


            // Place children in the parent layout
            placeables.forEach { placeable ->
                Log.w("logger-anim", "constraints ma width: ${constraints.maxWidth} - placeable.width ${placeable.width} = ${constraints.maxWidth - placeable.measuredWidth}")
                scope.launch { x.animateTo(if (leftSide) 0f else (constraints.maxWidth - placeable.width).toFloat()) }
                Log.w("logg_anim", "current x: ${x.value}")
                        // Position item on the screen
                placeable.placeRelative(x.value.toInt(), y = 50)
            }
        }
    }

}
