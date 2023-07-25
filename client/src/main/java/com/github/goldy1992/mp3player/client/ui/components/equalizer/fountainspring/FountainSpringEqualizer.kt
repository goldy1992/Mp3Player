package com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private const val logTag = "FountainSpringEqualizer"

private const val MAX_FREQUENCY = 150

@Composable
fun FountainSpringEqualizer(modifier: Modifier = Modifier,
                            canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                            particleWidthPx : Float = 10f,
                            isPlayingProvider : () -> Boolean = {false},
                            insetPx : Float =70f,
                            frequencyPhasesProvider : () -> List<Float> = { emptyList() },
                            particleColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxParticlesPerSpring : Int = 15
 ) {
    val isPlaying = isPlayingProvider()
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
    var fountain : Fountain by remember(spawnPoints) {
        Log.i(logTag, "new particles map because spawn point")
        val mutableSpringList = mutableListOf<Spring>()
        spawnPoints.forEachIndexed { idx, spawnOffset -> mutableSpringList.add(Spring(idx, spawnOffset, listOf())) }
        mutableStateOf(Fountain(mutableSpringList, 0))
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            val currentFrame = awaitFrame()
            fountain = Fountain(fountain.springs, currentFrame)
        }
    }

    // LaunchedEffect for CREATING new particles
    LaunchedEffect(frequencyPhases) {
        this.launch {
        //Log.i(logTag, "frequencyPhases updated, new LaunchedEffect")
            val springs = fountain.springs.iterator()
            val newSprings = mutableListOf<Spring>()
            while (springs.hasNext()) {
                val currentSpring = springs.next()
                val currentParticleList = currentSpring.particles
                val newParticleList = currentParticleList.toMutableList()
                if (newParticleList.size < maxParticlesPerSpring) {
              //    Log.i(logTag, "adding new particle to frequency: ${currentSpring.index}")
                    val currentFreq = frequencyPhases[currentSpring.index]
                    val fraction = currentFreq / MAX_FREQUENCY
                    val hMax = canvasSize.heightPx * fraction
                //    Log.i(logTag, "hMax: $hMax")
                    val angle = Math.toRadians(Random.nextDouble(85.0, 95.0))
                    val initialVelocity = calculateInitialVelocityGivenHMax(hMax, angle)
                    newParticleList.add(
                        createParticle(
                            currentSpring.offset,
                            initialVelocity = initialVelocity,
                            hMax = hMax,
                            color = particleColor
                        )
                    )
                }
                newSprings.add(Spring(currentSpring.index, currentSpring.offset, newParticleList.toList()))
            }

            fountain = Fountain(newSprings, fountain.currentFrame)
          //  Log.i(logTag, "set new map after creating new particles")
        }
    }

    // LaunchedEffect for UPDATING and REMOVING particles.
    LaunchedEffect(fountain) {
        this.launch {
           // Log.i(logTag, "launching new while particles in map coroutine")
            while (isPlaying && fountain.hasParticles()) {
           //     Log.i(logTag, "while Particles in map, update particles")
                val currentFrame = awaitFrame()
                val timeDiffSinceLastFrame = currentFrame - fountain.currentFrame
                val springsIterator = fountain.springs.iterator()
                val newSprings = mutableListOf<Spring>()
                while (springsIterator.hasNext()) {
                    val currentSpring = springsIterator.next()
                    val newParticles : MutableList<Particle> = mutableListOf()
                    val particlesIterator = currentSpring.particles.iterator()
                    while (particlesIterator.hasNext()) {
                        val currentParticle = particlesIterator.next()
                        if (!shouldRemoveParticle(currentParticle)) {
                            newParticles.add(updateParticle(currentParticle, timeDiffSinceLastFrame))
                        } else {
                    //        Log.i(logTag, "removing particle of y: ${currentParticle.y}")
                        }
                    }
                    newSprings.add(Spring(currentSpring.index, currentSpring.offset, newParticles.toList()))
                }
                fountain = Fountain(newSprings.toList(), currentFrame)
           //     Log.i(logTag, "set new map after updating particles")

            }
        }
    }

    FountainSpringCanvas(
        modifier = modifier,
        fountain = fountain
    )
}


private fun shouldRemoveParticle(currentParticle: Particle) : Boolean {
    val isFalling = currentParticle.isFalling
    val currentY = currentParticle.y
    return isFalling && (currentY > currentParticle.removePoint)
}

@Composable
private fun FountainSpringCanvas(
    fountain : Fountain,
    modifier: Modifier = Modifier,
) {
    Canvas( modifier = modifier
        .fillMaxSize()
    ) {
        for (springs in fountain.springs) {
            for (particle in springs.particles) {
                drawCircleParticle(particle)
            }
        }
    }

}

private fun DrawScope.drawCircleParticle(particle: Particle) {
    drawCircle(
        color = particle.color,
        radius = particle.radius,
        center = Offset(particle.x, particle.y)
    )
}


private fun createParticle(startOffset: Offset,
                           initialVelocity : Float,
                           hMax: Float,
                           color : Color) : Particle {
    val startY = startOffset.y
    val startX = startOffset.x
    val radius = Random.nextDouble(2.0, 5.0)
    val angle = Random.nextDouble(85.0, 95.0)
    return Particle(
        x = startX,
        y = startY,
        hMax = hMax,
        isFalling = false,
        startPosition = Offset(startX, startY),
        initialVelocity = initialVelocity,
        radius = radius.toFloat(),
        color = color,
        angle = angle)
}

private const val g = -9.8f

private fun updateParticle(particle: Particle, timeDiffNanoSecs : Long) : Particle {
    val elapsedTimeSecs = particle.elapsedTimeDeciSeconds + convertNanoSecondsToRuntimeSpeed(timeDiffNanoSecs)
    val x = particle.startPosition.x + (particle.initialVelocity * cos(Math.toRadians(particle.angle)).toFloat() * particle.elapsedTimeDeciSeconds)
    val y = particle.startPosition.y + ((particle.initialVelocity * sin(Math.toRadians(particle.angle)).toFloat() * particle.elapsedTimeDeciSeconds) - ((g * particle.elapsedTimeDeciSeconds * particle.elapsedTimeDeciSeconds) / 2f))

    val newAlpha = if (elapsedTimeSecs > 18f) 0f else (1-(elapsedTimeSecs / 18f))
    return Particle(
        startPosition = particle.startPosition,
        initialVelocity = particle.initialVelocity,
        isFalling = y > particle.y,
        x = x,
        y = y,
        elapsedTimeDeciSeconds = elapsedTimeSecs,
        color = Color(
                    red=particle.color.red,
                    green=particle.color.green,
                    blue= particle.color.blue,
                    alpha=newAlpha),
        angle = particle.angle,
        hMax = particle.hMax,
        radius = particle.radius)
}

private fun generateSpawnPoints(canvasWidth : Float,
                                canvasHeight : Float,
                                insetPx: Float,
                                particleWidth: Float,
                                numberOfFrequencies : Int) : List<Offset> {
    val spawnHeight : Float = canvasHeight //+ 10f
    val lengthOfDivision : Float = canvasWidth - (insetPx * 2) - (particleWidth * numberOfFrequencies)
    val widthBetweenSpawns : Float = lengthOfDivision / (numberOfFrequencies + 1)//if (numberOfFrequencies <= 1) lengthOfDivision else lengthOfDivision / (numberOfFrequencies - 1)

    val toReturn = arrayListOf<Offset>()
    for (i in 1 .. numberOfFrequencies) {
        val offsetValue = Offset(x = insetPx + (widthBetweenSpawns * i) + (particleWidth * i),
                            y = spawnHeight)
        toReturn.add(offsetValue)
    }
    return toReturn.toList()
}

/**
 *  Calculates the initial velocity, u, required to reach the max height
 *  i.e hMax = ((u^2)*(sin(x)^2)) / 2g (see google docs)
 *  => u = sqrt( abs((hMax * 2g) / (sin^2(x)))  )
 *  abs() is used to ensure NaN is not returned.
 *
 *  where hMax :- the maximum particle height
 *        x    :- the angle of projection
 *        g    :- the constant acceleration of gravity 9.8 m/s^2
 *        u    :- the initial velocity at the time of projection
 *
 * @param hMax The maximum height the particle should reach i.e. the velocity on the y axis is 0
 * @param angle THe angle of projection about the x-axis in radians.
 * @return u, the initial velocity at the time of projection.
 */
private fun calculateInitialVelocityGivenHMax(hMax : Float, angle : Double) : Float {
    val denominator = sin(angle).pow(2)
    val numerator = hMax * 2 * g // g is negative therefore we should
    val u =  sqrt(abs(numerator / denominator)).toFloat()
    return u * -1


}

//private fun particlesInMap(particleMap : Map<Int, List<Particle>>) : Boolean {
//    if (particleMap.isEmpty()) {
//      //  Log.i(logTag, "particles map empty, returning false")
//        return false
//    }
//
//    particleMap.entries.forEach { entry ->
//        if (isNotEmpty(entry.value)) {
//      //      Log.i(logTag, "non-empty list present,, returning true")
//            return true
//        }
//    }
// //   Log.i(logTag, "defaulting to particlesInMap: false")
//    return false
//}

/**
 * From nano seconds (i.e. 10^-9)
 * Runtime speed is Deci-seconds (i.e. 10^-1)
 */
private fun convertNanoSecondsToRuntimeSpeed(valueNs: Long) : Float {
    return valueNs * 10.0.pow(-8.0).toFloat()
}

private fun updateParticlesWithCurrentFrame(particles : Map<Int, List<Particle>>, frame : Long) : Map<Int, List<Particle>> {
    val toReturn = mutableMapOf<Int, List<Particle>>()
    particles.entries.forEach {
        val newList = mutableListOf<Particle>()
        val currentParticleList = it.value
        for (p in currentParticleList) {
            newList.add(Particle(
                 startPosition = p.startPosition,
                 hMax = p.hMax,
                 isFalling = p.isFalling,
                 initialVelocity = p.initialVelocity,
                 angle = p.angle,
                 radius =p.radius,
                 width = p.width,
                 color = p.color,
                 x = p.x,
                 y = p.y,
                 elapsedTimeDeciSeconds = p.elapsedTimeDeciSeconds,
            ))
        }
        toReturn[it.key] = newList
    }
    return toReturn
}

