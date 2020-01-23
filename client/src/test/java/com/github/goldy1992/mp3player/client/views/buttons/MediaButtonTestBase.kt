package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.os.Handler
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.nhaarman.mockitokotlin2.mock

open class MediaButtonTestBase {
    /**
     * mock MediaControllerAdapter
     */

    protected val mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()

    /**
     * Context
     */
    protected lateinit var context: Context

    /**
     * setup
     */
    protected open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }
}