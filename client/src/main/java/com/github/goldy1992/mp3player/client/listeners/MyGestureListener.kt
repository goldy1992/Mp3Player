package com.github.goldy1992.mp3player.client.listeners

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.views.adapters.MediaItemRecyclerViewAdapter
import javax.inject.Inject

class MyGestureListener

    @Inject
    constructor(
            private var itemSelectedListener : MyGenericItemTouchListener.ItemSelectedListener,
            var parentView: RecyclerView) :
        GestureDetector.SimpleOnGestureListener() {

     override fun onScroll(e1: MotionEvent, e2: MotionEvent,
                          distanceX: Float, distanceY: Float): Boolean {
        super.onScroll(e1, e2, distanceX, distanceY)
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        super.onSingleTapConfirmed(e)
        val childView = parentView.findChildViewUnder(e.x, e.y)
        if (null != childView) {
            val childPosition = parentView.getChildAdapterPosition(childView)
            val myViewAdapter = parentView.adapter as MediaItemRecyclerViewAdapter?
            if (null != myViewAdapter) {
                itemSelectedListener.itemSelected(myViewAdapter.items[childPosition])
            }
        }
        return false
    }

}