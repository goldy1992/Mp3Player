package com.github.goldy1992.mp3player.client

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.github.goldy1992.mp3player.client.views.adapters.MediaItemRecyclerViewAdapter
import javax.inject.Inject

class MyGenericItemTouchListener @Inject constructor(context: Context?, itemSelectedListener: ItemSelectedListener) : SimpleOnGestureListener(), OnItemTouchListener {
    private val gestureDetector: GestureDetector
    val itemSelectedListener: ItemSelectedListener
    var parentView: RecyclerView? = null
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

    fun setParentView(parentView: RecyclerView?) {
        this.parentView = parentView
    }

    interface ItemSelectedListener {
        fun itemSelected(item: MediaBrowserCompat.MediaItem?)
    }

    companion object {
        private const val LOG_TAG = "MY_ITEM_TOUCH_LISTENER"
    }

    init {
        gestureDetector = GestureDetector(context, this)
        this.itemSelectedListener = itemSelectedListener
    }
}