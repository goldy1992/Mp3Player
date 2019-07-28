package com.example.mike.mp3player.client;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

public abstract class MyGenericItemTouchListener extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener  {

    private static final String LOG_TAG = "MY_ITEM_TOUCH_LISTENER";
    private GestureDetector gestureDetector;
    RecyclerView parentView;
    View childView;
    ItemSelectedListener itemSelectedListener = null;



    public MyGenericItemTouchListener(Context context, ItemSelectedListener itemSelectedListener) {
        this.gestureDetector = new GestureDetector(context, this);
        this.itemSelectedListener = itemSelectedListener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            parentView = view;
            childView = view.findChildViewUnder(e.getX(), e.getY());
            gestureDetector.onTouchEvent(e);
            // return false in order to keep dispatching event
            return false;
        // return true so message is not re-dispatched

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            childView = rv.findChildViewUnder(e.getX(), e.getY());

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {  }


    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        super.onScroll(e1, e2, distanceX, distanceY);
        return false;
    }

    public interface ItemSelectedListener {
        void itemSelected(MediaBrowserCompat.MediaItem item);
    }
}
