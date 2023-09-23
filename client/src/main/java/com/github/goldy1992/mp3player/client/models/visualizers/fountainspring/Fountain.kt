package com.github.goldy1992.mp3player.client.models.visualizers.fountainspring

data class Fountain

    constructor(val springs : List<Spring>,
                val currentFrame : Long) {

    fun hasParticles() : Boolean {
        if (springs.isEmpty()) {
            return false
        }

        for (spring in springs) {
            if (spring.particles.isNotEmpty()) {
                return true
            }
        }
        return false
    }

}

