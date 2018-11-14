package com.example.mike.mp3player.client;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

public class MyItemTouchListener implements RecyclerView.OnItemTouchListener {

    private final MainActivity parentAtivity;

    public MyItemTouchListener(MainActivity parentAtivity) {
        this.parentAtivity = parentAtivity;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (null != childView) {
            int childPosition = view.getChildAdapterPosition(childView);
            MyViewAdapter myViewAdapter = (MyViewAdapter) view.getAdapter();
            String mediaId = myViewAdapter.getSongs().get(childPosition).getDescription().getMediaId();
            parentAtivity.callPlayerView(mediaId);
            return  true;
        }
        return  false;

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
