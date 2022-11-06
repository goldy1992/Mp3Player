package com.github.goldy1992.mp3player.client.viewmodels.states

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class ViewModelFlowState<T>

    protected constructor(
        protected val scope: CoroutineScope,
        protected val flow : Flow<T>,
        initialValue : T)
{

    protected val backingState = MutableStateFlow(initialValue)
    val state : StateFlow<T> = backingState

    init {
        scope.launch {
            flow.collect {
                backingState.value = it
            }
        }
    }

    protected abstract suspend fun initialValue() : T

}