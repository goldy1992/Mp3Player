package com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import kotlin.math.*
import kotlin.random.Random

private const val logTag = "FireworksEqualizer"

private const val MAX_FREQUENCY = 150

private var maxFreqVal = 0f
@Composable
fun FountainSpringEqualizer(modifier: Modifier = Modifier,
                            canvasSize : DpPxSize = DpPxSize.createDpPxSizeFromDp(200.dp, 200.dp, LocalDensity.current),
                            particleWidthPx : Float = 10f,
                            insetPx : Float =70f,
                            frequencyPhasesProvider : () -> List<Float> = { emptyList() },
                            particleColor : Color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxParticlesPerSpring : Int = 15
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

    // LaunchedEffect for CREATING new particles
    LaunchedEffect(frequencyPhases) {
        this.launch {
                Log.i(logTag, "frequencyPhases updated, new launchedeffect")
            val entryIterator = particles.entries.iterator()
            val newMap = mutableMapOf<Int, List<Particle>>()
            val frame = awaitFrame()
            while (entryIterator.hasNext()) {
                val entry = entryIterator.next()
                val currentParticleList = entry.value
                val newParticleList = currentParticleList.toMutableList()
                if (newParticleList.size < maxParticlesPerSpring) {
//                    Log.i(logTag, "adding new particle to freq: ${entry.key}")
                    val currentFreq = frequencyPhases[entry.key]
                    val frac = currentFreq / MAX_FREQUENCY
                    val hmax = canvasSize.heightPx * frac
                    Log.i(logTag, "hmax: $hmax")
                    val angle = Math.toRadians(Random.nextDouble(85.0, 95.0))
                    val initialVelocity = calculateInitialVelocityGivenHmax(hmax, angle)
                    newParticleList.add(
                        createParticle(
                            spawnPoints[entry.key], initialVelocity = initialVelocity,
                            currentFrame = frame,
                            hmax = hmax,
                            color = particleColor
                        )
                    )
                }
                newMap[entry.key] = newParticleList.toList()
            }
            particles = newMap
            Log.i(logTag, "set new map after creating new particles")
        }
    }

    // LaunchedEffect for UPDATING and REMOVING particles.
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
                        if (!shouldRemoveParticle(currentParticle)) {
                            newList.add(updateParticle(currentParticle, frame))
                        } else {
                            Log.i(logTag, "removing particle of y: ${currentParticle.y}")
                        }
                    }
                    newMap[entry.key] = newList.toList()
                }
                particles = newMap
                Log.i(logTag, "set new map after updating particles")

            }
        }
    }

    FountainSpringCanvas(
        modifier = modifier,
        particles = particles
    )
}


private fun shouldRemoveParticle(currentParticle: Particle) : Boolean {
    val isFalling = currentParticle.isFalling
    val currentY = currentParticle.y
    val startY = currentParticle.startPosition.y
    val hmax = currentParticle.hMax
    val removePoint = (startY * 0.9f)
    val result = currentParticle.isFalling && (currentY > removePoint)
    Log.i(logTag, "should remove particle: $result because, isFalling: $isFalling, currentY: $currentY, startY: $startY hmax: $hmax remove at point: $removePoint, elapsedTime: ${currentParticle.elapsedTimeDeciseconds}")
    return result
}

@Composable
private fun FountainSpringCanvas(
    particles : Map<Int, List<Particle>>,
    modifier: Modifier = Modifier,
    surfaceColor : Color = MaterialTheme.colorScheme.primaryContainer
) {
    Canvas( modifier = modifier
        .fillMaxSize()
    ) {
        drawRoundRect(color = surfaceColor, size = this.size, cornerRadius = CornerRadius(5f, 5f))

        for (particleLists in particles.values) {
            for (p in particleLists) {
                drawCircleParticle(p)
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


private fun DrawScope.drawLineParticle(particle: Particle, brush : Brush) {
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

private fun createParticle(startOffset: Offset,
                           initialVelocity : Float,
                           hmax: Float,
                           currentFrame : Long,
                           color : Color) : Particle {
    val startY = startOffset.y
    val startX = startOffset.x
    val radius = Random.nextDouble(2.0, 5.0)
    val angle = Random.nextDouble(85.0, 95.0)
    return Particle(
        x = startX,
        y = startY,
        hMax = hmax,
        isFalling = false,
        currentFrameTimeNs = currentFrame,
        startPosition = Offset(startX, startY),
        initialVelocity = initialVelocity.toFloat(),
        radius = radius.toFloat(),
        color = color,
        angle = angle)
}

private const val g = -9.8f

private fun updateParticle(particle: Particle, frame : Long) : Particle {
    var currentFrameTime = 0L
    var elapsedTimeSecs : Float = 0f
    var x : Float = 0f
    var y : Float = 0f

    val timeDiffNanoSecs = frame - particle.currentFrameTimeNs
    elapsedTimeSecs = particle.elapsedTimeDeciseconds + convertNanoSecondsToRuntimeSpeed(timeDiffNanoSecs)
    currentFrameTime = frame
    x = particle.startPosition.x + (particle.initialVelocity * cos(Math.toRadians(particle.angle)).toFloat() * particle.elapsedTimeDeciseconds)
    y = particle.startPosition.y + ((particle.initialVelocity * sin(Math.toRadians(particle.angle)).toFloat() * particle.elapsedTimeDeciseconds) - ((g * particle.elapsedTimeDeciseconds * particle.elapsedTimeDeciseconds) / 2f))

   val newAlpha = if (elapsedTimeSecs > 18f) 0f else (1-(elapsedTimeSecs / 18f))
    return Particle(
        startPosition = particle.startPosition,
        initialVelocity = particle.initialVelocity,
        isFalling = y > particle.y,
        currentFrameTimeNs = currentFrameTime,
        x = x,
        y = y,
        elapsedTimeDeciseconds = elapsedTimeSecs,
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

private fun nanoSecondsToMilliseconds(valueNs : Long) : Float {
    return valueNs / 10.0.pow(6.0).toFloat()
}

private fun nanoSecondsToSeconds(valueNs : Long) : Float {
    return valueNs / 10.0.pow(9.0).toFloat()
}

/**
 * From nano seconds (i.e. 10^-9)
 * Runtime speed is Deciseconds (i.e. 10^-1)
 */
private fun convertNanoSecondsToRuntimeSpeed(valueNs: Long) : Float {
    return valueNs * 10.0.pow(-8.0).toFloat()
}

