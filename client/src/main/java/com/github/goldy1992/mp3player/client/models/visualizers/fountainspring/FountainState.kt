package com.github.goldy1992.mp3player.client.models.visualizers.fountainspring

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlin.reflect.KProperty

object FountainState {

    operator fun State<Fountain>.getValue(thisObj: Any?, property: KProperty<*>): Fountain = value

    operator fun MutableState<Fountain>.setValue(thisObj: Any?, property: KProperty<*>, value: Fountain) {
        this.value = value
    }
}