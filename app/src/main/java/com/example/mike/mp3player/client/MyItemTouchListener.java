package com.example.mike.mp3player.client;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MyItemTouchListener implements RecyclerView.OnItemTouchListener {

    private final MainActivity parentAtivity;
    private GestureDetector gestureDetector;
    private RecyclerView parentView;
    private View childView;

    public MyItemTouchListener(MainActivity parentAtivity) {
        this.parentAtivity = parentAtivity;
        this.gestureDetector = new GestureDetector(parentAtivity.getApplicationContext(),
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        super.onSingleTapConfirmed(e);
                        if (null != childView) {
                            int childPosition = parentView.getChildAdapterPosition(childView);
                            MyViewAdapter myViewAdapter = (MyViewAdapter) parentView.getAdapter();
                            String mediaId = myViewAdapter.getSongs().get(childPosition).getDescription().getMediaId();
                            parentAtivity.callPlayerView(mediaId);
                        }
                        return false;
                    }
                });
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
    public void onTouchEvent(RecyclerView view, MotionEvent e) {
        parentView = view;
        childView = view.findChildViewUnder(e.getX(), e.getY());
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
