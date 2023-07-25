package com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlin.reflect.KProperty

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

operator fun  State<Fountain>.getValue(thisObj: Any?, property: KProperty<*>): Fountain = value

operator fun MutableState<Fountain>.setValue(thisObj: Any?, property: KProperty<*>, value: Fountain) {
    this.value = value
}