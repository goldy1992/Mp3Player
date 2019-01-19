package com.example.mike.mp3player.client;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.mike.mp3player.client.views.MediaPlayerActionListener;
import com.example.mike.mp3player.client.views.MyRecyclerView;

public class MyItemTouchListener extends GestureDetector.SimpleOnGestureListener implements RecyclerView.OnItemTouchListener {

    private static final String LOG_TAG = "MY_ITEM_TOUCH_LISTENER";
    private GestureDetector gestureDetector;
    private RecyclerView parentView;
    private View childView;
    private MyRecyclerView myRecyclerView;
    private MediaPlayerActionListener mediaPlayerActionListener = null;
    private boolean enabled = true;

    public MyItemTouchListener(Context context) {
        this.gestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        if (enabled) {
            parentView = view;
            childView = view.findChildViewUnder(e.getX(), e.getY());
            gestureDetector.onTouchEvent(e);
            // return false in order to keep dispatching event
            return false;
        }
        // return true so message is not re-dispatched
        return true;

    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (rv instanceof MyRecyclerView) {
            this.myRecyclerView = (MyRecyclerView) rv;
            childView = rv.findChildViewUnder(e.getX(), e.getY());
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MyViewAdapter myViewAdapter = (MyViewAdapter) parentView.getAdapter();
            String mediaId = myViewAdapter.getFilteredSongs().get(childPosition).getDescription().getMediaId();
            this.mediaPlayerActionListener.playSelectedSong(mediaId);
        }
        return false;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        super.onScroll(e1, e2, distanceX, distanceY);
        return false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMediaPlayerActionListener(MediaPlayerActionListener mediaPlayerActionListener) {
        this.mediaPlayerActionListener = mediaPlayerActionListener;
    }
}
