package com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.screens.DpPxSize
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import kotlin.math.*
import kotlin.random.Random

private const val logTag = "FireworksEqualizer"

private const val MAX_FREQUENCY = 150

private var maxFreqVal = 0f
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FireworkWrapperNew(modifier: Modifier = Modifier,
                    canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                    particleWidthPx : Float = 10f,
                    insetPx : Float =70f,
                    frequencyPhasesProvider : () -> List<Float> = { emptyList() },
                    particleColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
 ) {
    val frequencyPhases = frequencyPhasesProvider()
   // Log.i(logTag, "recomposing")
    val spawnPoints : List<Offset> = remember(insetPx, canvasSize, particleWidthPx, frequencyPhases.size) {
        generateSpawnPoints(canvasWidth = canvasSize.widthPx,
            canvasHeight = canvasSize.heightPx,
            insetPx = insetPx,
            particleWidth = particleWidthPx,
            numberOfFrequencies = frequencyPhases.size
        ) }
   // Log.i(logTag, "spawnPoints: $spawnPoints")

    var particles : Map<Int, List<Particle>> by remember(spawnPoints) {
        Log.i(logTag, "new particles map")
        val map : MutableMap<Int, List<Particle>> = mutableMapOf()
        spawnPoints.indices.forEach { idx -> map[idx] = listOf() }
        mutableStateOf(map.toMap())
    }

    // TODO: separate into 2 separate flows, 1. add new particles 2. update animation
    LaunchedEffect(frequencyPhases) {
        this.launch {
                Log.i(logTag, "frequencyPhases updated, new launchedeffect")
            val entryIterator = particles.entries.iterator()
            val newMap = mutableMapOf<Int, List<Particle>>()
            while (entryIterator.hasNext()) {
                val entry = entryIterator.next()
                val currentParticleList = entry.value
                val newParticleList = currentParticleList.toMutableList()
                if (newParticleList.size < 15) {
//                    Log.i(logTag, "adding new particle to freq: ${entry.key}")
                    val currentFreq = frequencyPhases[entry.key]
                    val frac = currentFreq / MAX_FREQUENCY
                    val hmax = canvasSize.heightPx * frac
                    val angle = Math.toRadians(Random.nextDouble(85.0, 95.0))
                    val initialVelocity = calculateInitialVelocityGivenHmax(hmax, angle)

                    newParticleList.add(
                        createParticle(
                            spawnPoints[entry.key], initialVelocity = initialVelocity,
                            color = if (entry.key % 2 == 0) Color.Blue else particleColor
                        )

                    )
                }
                newMap[entry.key] = newParticleList.toList()
            }
            particles = newMap
            Log.i(logTag, "set new map after creating new particles")
        }
    }


    LaunchedEffect(particles) {
        this.launch {
            Log.i(logTag, "launching new while particles in map corouting")
            while (particlesInMap(particles)) {
                Log.i(logTag, "while Particles in map, update particles")
                val frame = awaitFrame()
                val entryIterator = particles.entries.iterator()
                val newMap = mutableMapOf<Int, List<Particle>>()
                while (entryIterator.hasNext()) {
                    val entry = entryIterator.next()
                    val newList : MutableList<Particle> = mutableListOf()
                    val valueIterator = entry.value.iterator()
                    while (valueIterator.hasNext()) {
                        val currentParticle = valueIterator.next()
                        if (currentParticle.y < (canvasSize.heightPx + 10)) {
                            Log.i(logTag, "updatingparticle")
                            newList.add(updateParticle(currentParticle, frame))
                        }
                    }
                    newMap[entry.key] = newList.toList()
                }
                particles = newMap
                Log.i(logTag, "set new map after updating particles")

            }
        }
    }

    FireworkEqualizer(
        modifier = modifier,
        particles = particles
    )
//    {
//            width, height ->
//            canvasWidth = width
//            canvasHeight = height
//    }
}

@Composable
private fun FireworkEqualizer(
    particles : Map<Int, List<Particle>>,
    modifier: Modifier = Modifier,
    surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer
) {
//    var msg = "equalizer called with: "
//    for (p in particles.entries) {
//        msg += "${p.key}: ${p.value}\n"
//    }
//    Log.i(logTag, msg)
    Canvas( modifier = modifier
        .fillMaxSize()
    ) {
        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))

        for (particleLists in particles.values) {
            for (p in particleLists) {
                drawParticle(p)
            }
        }
    }

}


private fun DrawScope.drawParticle(particle: Particle, brush : Brush) {
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

private fun DrawScope.drawParticle(particle: Particle) {
    drawLine(color = particle.color,
        start = Offset(particle.x, particle.y),
        strokeWidth = 10f,
        end = Offset(particle.x, particle.y + 40)
    )
}

private fun createParticle(startOffset: Offset, initialVelocity : Float, color : Color) : Particle {
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

private fun updateParticle(particle: Particle, frame : Long) : Particle {
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
    val spawnHeight : Float = canvasHeight //+ 10f
    val widthBetweenSpawns : Float = (canvasWidth - (insetPx * 2) - (particleWidth * numberOfFrequencies)) / (numberOfFrequencies - 1)

    val toReturn = arrayListOf<Offset>()
    for (i in 0 until numberOfFrequencies) {
        val offsetValue = Offset(x = insetPx + (widthBetweenSpawns * i) + (particleWidth * i),
                            y = spawnHeight)
        toReturn.add(offsetValue)
    }
    return toReturn.toList()
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
        Log.i(logTag, "particles map empty, returning false")
        return false
    }

    particleMap.entries.forEach { entry ->
        if (isNotEmpty(entry.value)) {
            Log.i(logTag, "non-empty list present,, returning true")
            return true
        }
    }
    Log.i(logTag, "defaulting to particlesInMap: false")
    return false
}

