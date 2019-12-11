package com.github.goldy1992.mp3player.client.listeners

import android.support.v4.media.MediaBrowserCompat
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

class MyGenericItemTouchListener

    @Inject
    constructor(
            private var gestureDetector: GestureDetectorCompat,
            itemSelectedListener: ItemSelectedListener)
    : LogTagger, OnItemTouchListener {


    /**
     * @param view the RecyclerView
     * @param e the motion event object
     * @return false in order to keep dispatching event, true so message is not re-dispatched
     */
    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}



    interface ItemSelectedListener {
        fun itemSelected(item: MediaBrowserCompat.MediaItem?)
    }

    override fun logTag(): String {
        return "MY_ITEM_TOUCH_LISTENER"
    }

}