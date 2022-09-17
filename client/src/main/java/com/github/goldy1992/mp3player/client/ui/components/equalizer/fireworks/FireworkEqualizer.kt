package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.android.awaitFrame
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import kotlin.math.*
import kotlin.random.Random

private const val logTag = "FireworksEqualizer"

private const val MAX_FREQUENCY = 150

private var maxFreqVal = 0f
@Composable
fun FireworkWrapper(modifier: Modifier = Modifier,
                    particleWidth : Float = 10f,
                    isPlaying : Boolean = false,
                    insetPx : Float = 200f,
                    frequencyPhases : List<Float> = emptyList(),
 ) {

    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }
    val spawnPoints : List<Offset> = remember(insetPx, canvasWidth, canvasHeight, particleWidth, frequencyPhases.size) {
        generateSpawnPoints(canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            insetPx = insetPx,
            particleWidth = particleWidth,
            numberOfFrequencies = frequencyPhases.size
        ) }

    val particles : MutableState<Map<Int, List<Particle>>> = remember { mutableStateOf(emptyMap()) }

    LaunchedEffect(frequencyPhases, particles, isPlaying) {

        while(isPlaying || particlesInMap(particles.value)) {
            val newParticleMap = HashMap<Int, ArrayList<Particle>>()
            val frame = awaitFrame()

            for (i in 1 .. frequencyPhases.size) {
                newParticleMap[i] = arrayListOf()
            }

            particles.value.entries.forEach { entry ->
                entry.value.forEach { value ->
                    if (value.y < (canvasHeight + 100)) {
                        newParticleMap[entry.key]?.add(updateParticle(value, frame))
                    }
                }
            }

            newParticleMap.entries.forEach { entry ->
                if (isPlaying) {
                    val currentFreq = frequencyPhases[entry.key - 1]
                    val frac = currentFreq / MAX_FREQUENCY
                    val hmax = canvasHeight * frac
                    val angle = Math.toRadians(Random.nextDouble(85.0, 95.0))
                    val initialVelocity = calculateInitialVelocityGivenHmax(hmax, angle)
                    entry.value.add(
                        createParticle(spawnPoints[entry.key -1], initialVelocity = initialVelocity,
                            color = if (entry.key % 2 == 0) Color.Blue else Color.Green)
                    )
                }
            }
            particles.value = newParticleMap
        }
    }

    FireworkEqualizer(
        modifier = modifier,
        particles = particles.value) {
            width, height ->
            canvasWidth = width
            canvasHeight = height
    }
}

@Composable
fun FireworkEqualizer(
    particles : Map<Int, List<Particle>>,
    modifier: Modifier = Modifier,
    onOffsetChanged : (width : Float, height : Float) -> Unit) {

    Canvas( modifier = modifier
        .fillMaxSize()
        .background(Color.Red)
        .onSizeChanged {
            onOffsetChanged(it.width.toFloat(), it.height.toFloat())
        }) {

        for (particleLists in particles.values) {
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

private const val g = -9.8f

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
                                particleWidth: Float,
                                numberOfFrequencies : Int) : List<Offset> {
    val spawnHeight : Float = canvasHeight + 10f
    val widthBetweenSpawns : Float = (canvasWidth - (insetPx * 2) - (particleWidth * numberOfFrequencies)) / (numberOfFrequencies - 1)

    val toReturn = arrayListOf<Offset>()
    for (i in 0 until numberOfFrequencies) {
        val offsetValue = Offset(x = insetPx + (widthBetweenSpawns * i) + (particleWidth * i),
                            y = spawnHeight)
        toReturn.add(offsetValue)
    }
    return toReturn
}

/**
 *  Calculates the initial velocity, u, required to reach the max height
 *  i.e hmax = ((u^2)*(sin(x)^2)) / 2g (see google docs)
 *  => u = sqrt( abs((hmax * 2g) / (sin^2(x)))  )
 *  abs() is used to ensure NaN is not returned.
 *
 *  where hmax :- the maximum particle height
 *        x    :- the angle of projection
 *        g    :- the constant acceleration of gravity 9.8 m/s^2
 *        u    :- the initial velocity at the time of projection
 *
 * @param hmax The maximum height the particle should reach i.e. the velocity on the y axis is 0
 * @param angle THe angle of projection about the x-axis in radians.
 * @return u, the initial velocity at the time of projection.
 */
private fun calculateInitialVelocityGivenHmax(hmax : Float, angle : Double) : Float {
    val denominator = sin(angle).pow(2)
    val numerator = hmax * 2 * g // g is negative therefore we should
    val u =  sqrt(abs(numerator / denominator)).toFloat()
    return u * -1


}

private fun particlesInMap(particleMap : Map<Int, List<Particle>>) : Boolean {
    if (particleMap.isEmpty()) {
        return false
    }

    particleMap.entries.forEach { entry ->
        if (isNotEmpty(entry.value)) {
            return true
        }
    }
    return false
}

