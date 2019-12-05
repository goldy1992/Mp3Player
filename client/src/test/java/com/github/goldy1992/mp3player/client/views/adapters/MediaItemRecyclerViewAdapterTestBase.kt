package com.github.goldy1992.mp3player.client.views.adapters

import android.content.Context
import android.os.Handler
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito

open class MediaItemRecyclerViewAdapterTestBase {
    @Mock
    lateinit var handler: Handler
    @Mock
    var albumArtPainter: AlbumArtPainter? = null
    var context: Context? = null
    var viewGroup: ViewGroup? = null
    var mediaItems: MutableList<MediaItem> = mutableListOf()

    open fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
        viewGroup = mockViewGroup
    }

    private val mockViewGroup: ViewGroup
        get() {
            val viewGroup = Mockito.mock(ViewGroup::class.java)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            Mockito.`when`(viewGroup.generateLayoutParams(ArgumentMatchers.any(AttributeSet::class.java))).thenReturn(layoutParams)
            Mockito.`when`(viewGroup.context).thenReturn(context)
            return viewGroup
        }
}