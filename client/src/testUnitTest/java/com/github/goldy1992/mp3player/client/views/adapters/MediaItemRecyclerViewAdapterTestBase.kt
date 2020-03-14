package com.github.goldy1992.mp3player.client.views.adapters

import android.content.Context
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

open class MediaItemRecyclerViewAdapterTestBase {
    var albumArtPainter: AlbumArtPainter = mock<AlbumArtPainter>()
    lateinit var context: Context
    lateinit var viewGroup: ViewGroup
    var mediaItems: MutableList<MediaItem> = mutableListOf()

    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        viewGroup = mockViewGroup
    }

    private val mockViewGroup: ViewGroup
        get() {
            val viewGroup = mock<ViewGroup>()
            val layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            whenever(viewGroup.generateLayoutParams(any<AttributeSet>())).thenReturn(layoutParams)
            whenever(viewGroup.context).thenReturn(context)
            return viewGroup
        }
}