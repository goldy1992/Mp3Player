package com.github.goldy1992.mp3player.client.media.flows

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MediaFlowTestBase<T> {

    private val testScheduler : TestCoroutineScheduler = TestCoroutineScheduler()

    protected val dispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    protected val testScope : TestScope = TestScope(dispatcher)
    private var resultState : MutableStateFlow<T>? = null


    fun setup() {

    }

    protected fun initTestFlow(defaultValue : T) : MutableStateFlow<T> {
        val resultState = MutableStateFlow(defaultValue)
        testScope.launch {
            // Add a collector to the resulting state flow to trigger the source
            resultState.collect { }
        }
        this.resultState = resultState
        return resultState
    }

    @After
    fun tearDown() {
        resultState?.onCompletion {  }
        testScope.advanceUntilIdle()
        testScope.cancel()
    }
}