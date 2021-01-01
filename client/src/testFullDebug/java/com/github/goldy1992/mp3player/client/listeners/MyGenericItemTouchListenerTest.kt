package com.github.goldy1992.mp3player.client.listeners

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.views.adapters.MediaItemListRecyclerViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.SongListAdapter
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyGenericItemTouchListenerTest {
    private val itemSelectedListener : ItemSelectedListener = mock<ItemSelectedListener>()

    private lateinit var myGenericItemTouchListener : MyGenericItemTouchListener

    @Before
    fun setup() {
        val context : Context = InstrumentationRegistry.getInstrumentation().context
        myGenericItemTouchListener = MyGenericItemTouchListener(context, itemSelectedListener)
    }

    @Test
    fun testOnScroll() {
        val motionEvent: MotionEvent = MotionEvent.obtain(0L, 0L, 0, 0f, 0f, 0);
        val result = this.myGenericItemTouchListener.onScroll(motionEvent, motionEvent, 0f, 0f)
        assertFalse(result)
    }

    @Test
    fun testOnSingleTapConfirmed() {
        val albumArtPainter : AlbumArtPainter = mock<AlbumArtPainter>()
        val expectedChildPosition = 0
        val motionEvent : MotionEvent = mock<MotionEvent>()
        val parentView : RecyclerView = mock<RecyclerView>()
        val childView : View = mock<View>()
        val viewAdapterList : MediaItemListRecyclerViewAdapter = SongListAdapter(albumArtPainter)
        val expectedMediaItem : MediaBrowserCompat.MediaItem = MediaItemBuilder("id").build()
        viewAdapterList.submitList(arrayListOf(expectedMediaItem))
        whenever(motionEvent.x).thenReturn(0f)
        whenever(motionEvent.y).thenReturn(0f)
        whenever(parentView.findChildViewUnder(motionEvent.x, motionEvent.y)).thenReturn(childView)
        whenever(parentView.getChildAdapterPosition(childView)).thenReturn(expectedChildPosition)
        whenever(parentView.adapter).thenReturn(viewAdapterList)
        myGenericItemTouchListener.parentView = parentView

        val result = myGenericItemTouchListener.onSingleTapConfirmed(motionEvent)
        assertFalse(result)
        verify(itemSelectedListener, times(1)).itemSelected(expectedMediaItem)
    }
}