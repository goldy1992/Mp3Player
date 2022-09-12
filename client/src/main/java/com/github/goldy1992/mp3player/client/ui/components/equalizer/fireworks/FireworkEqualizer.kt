package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.android.awaitFrame
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

private const val logTag = "FireworksEqualizer"

@Composable
fun FireworkWrapper(modifier: Modifier
 ) {

}

@Composable
fun FireworkEqualizer(modifier: Modifier = Modifier,
                      frequencyPhases : List<Float> = emptyList(),
                      insetPx : Float = 200f,
                      isActive : Boolean = false) {

    LocalLifecycleOwner.current.lifecycle.currentState
    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }
    val spawnPoints : List<Offset> = remember(insetPx, canvasWidth, canvasHeight, frequencyPhases.size) {
        generateSpawnPoints(canvasWidth = canvasWidth,
                            canvasHeight = canvasHeight,
                            insetPx = insetPx,
                            numberOfFrequencies = frequencyPhases.size
    ) }
    val particles : MutableState<Map<Int, List<Particle>>> = remember { mutableStateOf(emptyMap()) }

    LaunchedEffect(frequencyPhases, particles, isActive) {
        var isBlue = false
       // Log.i(logTag, "scope ${this.coroutineContext}")
        while(true) {
            //    Log.i(logTag, "while true")
            val newParticleMap = HashMap<Int, ArrayList<Particle>>()
            val frame = awaitFrame()

          //  Log.i(logTag, "frequencyPhases: ${frequencyPhases.size}")
            for (i in 1 until frequencyPhases.size) {
                newParticleMap[i] = arrayListOf()
            }

            particles.value.entries.forEach { entry ->

                entry.value.forEach {value ->
                    newParticleMap[entry.key]?.add(updateParticle(value, frame))
                }
            }

            newParticleMap.entries.forEach { entry ->
                val particleIterator = entry.value.iterator()
                while (particleIterator.hasNext()) {
                    val particle = particleIterator.next()
                    if (particle.y > (canvasHeight + 100)) {
                      //  Log.i(logTag, "removing particle")
                        particleIterator.remove()
                    }
                }

                if (isActive) {
                    val currentFreq = frequencyPhases[entry.key]
                    val frac = currentFreq / 500f
                    val initialVelocity = (80 + ((500-80) * frac)) *-1
                    // TODO: calculate velocity to reach the max height: hmax = ((u^2)*(sin(x)^2)) / 2g (see google docs)
               //     Log.i(logTag, "initialVelocoty: $initialVelocity")
                    entry.value.add(
                        createParticle(spawnPoints[entry.key -1], initialVelocity = initialVelocity,
                        color = if (entry.key % 2 == 0) Color.Blue else Color.Green)
                    )
                }
            }

            particles.value = newParticleMap
          //  Log.i(logTag, "newParticleMap: ${newParticleMap}, keys: ${newParticleMap.entries.size}")
        }
    }

    Canvas( modifier = Modifier
        .fillMaxSize()
        .background(Color.Red)
        .onSizeChanged {
            canvasWidth = it.width.toFloat()
            canvasHeight = it.height.toFloat()
        }) {
        val brush = Brush.horizontalGradient(listOf(Color.Black, Color.White))
        //  Log.i(logTag, "recompose canvas")
        for (particleLists in particles.value.values) {
            for (p in particleLists) {
                drawParticle(p)
            }
        }
    }

}


fun DrawScope.drawParticle(particle: Particle, brush : Brush) {
//    drawCircle(
//        //       color = particle.color,
//        brush = brush,
//        radius = particle.radius,
//        center = Offset(particle.x, particle.y)
//    )

    drawLine(brush = brush,
            start = Offset(particle.x, particle.y),
            strokeWidth = 10f,
            end = Offset(particle.x, particle.y + 5f)
    )
}

fun DrawScope.drawParticle(particle: Particle) {
    drawLine(color = particle.color,
        start = Offset(particle.x, particle.y),
        strokeWidth = 10f,
        end = Offset(particle.x, particle.y + 40)
    )
}

fun createParticle(startOffset: Offset) : Particle {
    val initialVelocity = Random.nextDouble(80.0, 90.0) * -1.0
    return createParticle(startOffset, initialVelocity.toFloat(), Color.Blue)
}
fun createParticle(startOffset: Offset, initialVelocity : Float, color : Color) : Particle {
    val startY = startOffset.y
    val startX = startOffset.x
    val radius = Random.nextDouble(30.0, 55.0)
    val angle = Random.nextDouble(85.0, 95.0)
    return Particle(
        x = startX,
        y = startY,
        startPosition = Offset(startX, startY),
        initialVelocity = initialVelocity.toFloat(),
        radius = radius.toFloat(),
        color = color,
        angle = angle)
}

fun updateParticle(particle: Particle, frame : Long) : Particle {
    var currentFrameTime = 0L
    var t : Float = 0f
    var x : Float = 0f
    var y : Float = 0f
    if (particle.currentFrameTime == 0L) {
        currentFrameTime = frame
    } else {
        val timeDiffNanoSecs = frame - particle.currentFrameTime
        // TODO: define time in seconds or milliseconds!
        t = particle.t + timeDiffNanoSecs * 10.0.pow(-8.0).toFloat()
        currentFrameTime = frame
        x = particle.startPosition.x + (particle.initialVelocity * cos(Math.toRadians(particle.angle)).toFloat() * particle.t)
        y = particle.startPosition.y + ((particle.initialVelocity * sin(Math.toRadians(particle.angle)).toFloat() * particle.t) - ((-9.8f * particle.t * particle.t) / 2f))

    }
   val newAlpha = if (t > 18f) 0f else (1-(t / 18f))
    return Particle(
        startPosition = particle.startPosition,
        initialVelocity = particle.initialVelocity,
        currentFrameTime = currentFrameTime,
        x = x,
        y = y,
        t = t,
        color = Color(
                    red=particle.color.red,
                    green=particle.color.green,
                    blue= particle.color.blue,
                    alpha=newAlpha),
        angle = particle.angle,
        radius = particle.radius)
}

private fun generateSpawnPoints(canvasWidth : Float,
                                canvasHeight : Float,
                                insetPx: Float,
                                numberOfFrequencies : Int) : List<Offset> {
  //  Log.i(logTag, "generating spawn Points")
    val spawnHeight : Float = canvasHeight + 10f
    val widthBetweenSpawns : Float = (canvasWidth - (insetPx * 2)) / (numberOfFrequencies + 1)

    val toReturn = arrayListOf<Offset>()
    for (i in 1 .. numberOfFrequencies) {
        val offsetValue = Offset(x = insetPx + (widthBetweenSpawns * i),
                            y = spawnHeight)
        toReturn.add(offsetValue)
    }

  //  Log.i(logTag, "spawn points: ${toReturn}")
    return toReturn
}

