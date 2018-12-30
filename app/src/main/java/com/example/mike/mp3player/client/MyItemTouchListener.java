package com.example.mike.mp3player.client;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.mike.mp3player.client.view.MyRecyclerView;

public class MyItemTouchListener extends GestureDetector.SimpleOnGestureListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private RecyclerView parentView;
    private View childView;
    private MyRecyclerView myRecyclerView;
    private MyRecycleViewSelectListener myRecycleViewSelectListener = null;
    private boolean isTouchable = true;

    public MyItemTouchListener(Context context) {
        this.gestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        parentView = view;
        childView = view.findChildViewUnder(e.getX(), e.getY());
        gestureDetector.onTouchEvent(e);
        // return false in order to keep dispatching event
        return false;

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
        if (isTouchable) {
            super.onSingleTapConfirmed(e);
            if (null != childView) {
                int childPosition = parentView.getChildAdapterPosition(childView);
                MyViewAdapter myViewAdapter = (MyViewAdapter) parentView.getAdapter();
                String mediaId = myViewAdapter.getFilteredSongs().get(childPosition).getDescription().getMediaId();
                this.myRecycleViewSelectListener.onItemSelected(mediaId);
            }
        }
        return false;
    }
    public boolean isTouchable() {
        return isTouchable;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        if (isTouchable) {
            super.onScroll(e1, e2, distanceX, distanceY);
        }
        return false;
    }

    public void setTouchable(boolean touchable) {
        isTouchable = touchable;
    }

    public MyRecycleViewSelectListener getMyRecycleViewSelectListener() {
        return myRecycleViewSelectListener;
    }

    public void setMyRecycleViewSelectListener(MyRecycleViewSelectListener myRecycleViewSelectListener) {
        this.myRecycleViewSelectListener = myRecycleViewSelectListener;
    }

    interface MyRecycleViewSelectListener {
        void onItemSelected(String id);
    }
}
