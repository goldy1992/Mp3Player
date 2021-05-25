package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewbinding.ViewBinding

abstract class MediaItemViewHolderTestBase<V : MediaItemViewHolder<ViewBinding>> {

    lateinit var mediaItemViewHolder : V

    lateinit var context: Context

    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }
}