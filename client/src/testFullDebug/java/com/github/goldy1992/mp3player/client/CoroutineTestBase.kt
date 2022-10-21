package com.github.goldy1992.mp3player.client

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
abstract class CoroutineTestBase {
    protected val testScheduler = TestCoroutineScheduler()
    protected val dispatcher  = StandardTestDispatcher(testScheduler)
    protected val testScope = TestScope(dispatcher)

}