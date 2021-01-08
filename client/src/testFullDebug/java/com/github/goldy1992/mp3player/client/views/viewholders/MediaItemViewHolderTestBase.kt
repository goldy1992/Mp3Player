package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewbinding.ViewBinding
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.nhaarman.mockitokotlin2.mock

abstract class MediaItemViewHolderTestBase<V : MediaItemViewHolder<ViewBinding>> {

    lateinit var albumArtPainter: AlbumArtPainter
    lateinit var mediaItemViewHolder : V

    lateinit var context: Context

    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        albumArtPainter = mock<AlbumArtPainter>()
    }
}