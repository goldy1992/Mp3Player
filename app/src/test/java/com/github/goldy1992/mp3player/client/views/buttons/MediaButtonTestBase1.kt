package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.os.Handler
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import org.mockito.Mock
import org.mockito.MockitoAnnotations

open class MediaButtonTestBase {
    /**
     * mock MediaControllerAdapter
     */
    @Mock
    protected var mediaControllerAdapter: MediaControllerAdapter? = null
    /**
     * The main handler used to update the GUI
     */
    @Mock
    protected var handler: Handler? = null
    /**
     * Context
     */
    protected var context: Context? = null

    /**
     * setup
     */
    protected open fun setup() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
    }
}