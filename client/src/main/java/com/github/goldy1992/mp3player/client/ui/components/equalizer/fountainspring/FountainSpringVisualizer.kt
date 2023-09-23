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
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.Fountain
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.FountainState.getValue
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.FountainState.setValue
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.Particle
import com.github.goldy1992.mp3player.client.models.visualizers.fountainspring.Spring
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.utils.visualizer.FountainSpringUtils.calculateInitialVelocityGivenHMax
import com.github.goldy1992.mp3player.client.utils.visualizer.FountainSpringUtils.generateSpringSpawnPoints
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val logTag = "FountainSpringEqualizer"

private const val MAX_FREQUENCY = 150

@Composable
fun FountainSpringVisualizer(modifier: Modifier = Modifier,
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
        generateSpringSpawnPoints(canvasWidth = canvasSize.widthPx,
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
                    Particle.createParticle(
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

    // LaunchedEffect for UPDATING and REMOVING particles.
    LaunchedEffect(fountain) {
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
                    if (!currentParticle.shouldRemove()) {
                        newParticles.add(currentParticle.update(timeDiffSinceLastFrame))
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

    FountainSpringCanvas(
        modifier = modifier,
        fountain = fountain
    )
}


@Composable
private fun FountainSpringCanvas(
    fountain : Fountain,
    modifier: Modifier = Modifier,
) {
    Canvas( modifier = modifier
        .fillMaxSize()
    ) {
        for (spring in fountain.springs) {
            for (particle in spring.particles) {
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
