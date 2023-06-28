package com.github.goldy1992.mp3player.client.media.flows

import android.os.Bundle
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class OnCustomCommandFlowTest : MediaBrowserListenerFlowTestBase<SessionCommandEventHolder>() {

    @Test
    fun testCollect() {

        val resultState = initTestFlow(SessionCommandEventHolder.DEFAULT)
        OnCustomCommandFlow.create(testScope, addListener,removeListener, collectLambda)
        val expectedAction = "TestAction"
        val testData = SessionCommand(expectedAction, Bundle())
        invoke { listener: MediaBrowser.Listener ->
            listener.onCustomCommand(
                controller, testData, Bundle())
        }
        testScope.advanceUntilIdle()

        val result = resultState.value
        assertEquals(expectedAction, result.command.customAction)
    }

}