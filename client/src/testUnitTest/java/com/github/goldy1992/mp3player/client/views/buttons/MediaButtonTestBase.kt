package com.github.goldy1992.mp3player.client.views.buttons

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.google.android.material.button.MaterialButton
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

open class MediaButtonTestBase {
    /**
     * mock MediaControllerAdapter
     */

    protected val mediaControllerAdapter: MediaControllerAdapter = mock<MediaControllerAdapter>()

    /**
     * Context
     */
    protected lateinit var context: Context

    protected val view : MaterialButton = mock<MaterialButton>()

    /**
     *
     */
    private val icon : Drawable = mock<Drawable>()

    /**
     * setup
     */
    protected open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        whenever(view.icon).thenReturn(icon)
    }
}