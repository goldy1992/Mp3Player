package com.github.goldy1992.mp3player.client.listeners

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.github.goldy1992.mp3player.client.views.adapters.MediaItemRecyclerViewAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

class MyGenericItemTouchListener

    @Inject
    constructor(
            private var context: Context,
            var itemSelectedListener : ItemSelectedListener)
    :  GestureDetector.SimpleOnGestureListener(), LogTagger, OnItemTouchListener {

    private var gestureDetector : GestureDetectorCompat
    var parentView: RecyclerView? = null

    init {
        this.gestureDetector = GestureDetectorCompat(context, this)
    }


    override fun onScroll(e1: MotionEvent, e2: MotionEvent,
                          distanceX: Float, distanceY: Float): Boolean {
        super.onScroll(e1, e2, distanceX, distanceY)
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        super.onSingleTapConfirmed(e)
        val childView = parentView!!.findChildViewUnder(e.x, e.y)
        if (null != childView) {
            val childPosition = parentView!!.getChildAdapterPosition(childView)
            val myViewAdapter = parentView!!.adapter as MediaItemRecyclerViewAdapter?
            if (null != myViewAdapter) {
                itemSelectedListener.itemSelected(myViewAdapter.items[childPosition])
            }
        }
        return false
    }


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