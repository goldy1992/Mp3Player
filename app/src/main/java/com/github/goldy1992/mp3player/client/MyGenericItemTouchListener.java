package com.github.goldy1992.mp3player.client;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.goldy1992.mp3player.client.views.adapters.MediaItemRecyclerViewAdapter;

public class MyGenericItemTouchListener extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener  {

    private static final String LOG_TAG = "MY_ITEM_TOUCH_LISTENER";
    private final GestureDetector gestureDetector;
    final ItemSelectedListener itemSelectedListener;
    RecyclerView parentView;

    public MyGenericItemTouchListener(Context context, ItemSelectedListener itemSelectedListener) {
        this.gestureDetector = new GestureDetector(context, this);
        this.itemSelectedListener = itemSelectedListener;
    }

    /**
     * @param view the RecyclerView
     * @param e the motion event object
     * @return false in order to keep dispatching event, true so message is not re-dispatched
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {  }


    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        super.onScroll(e1, e2, distanceX, distanceY);
        return false;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        View childView = parentView.findChildViewUnder(e.getX(), e.getY());
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MediaItemRecyclerViewAdapter myViewAdapter = (MediaItemRecyclerViewAdapter) parentView.getAdapter();
            if (null != myViewAdapter) {
                this.itemSelectedListener.itemSelected(myViewAdapter.getItems().get(childPosition));
            }
        }
        return false;
    }

    public void setParentView(RecyclerView parentView) {
        this.parentView = parentView;
    }

    public interface ItemSelectedListener {
        void itemSelected(MediaBrowserCompat.MediaItem item);
    }
}
