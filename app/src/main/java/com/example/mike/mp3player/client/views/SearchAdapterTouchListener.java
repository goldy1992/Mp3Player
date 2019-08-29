package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapterTouchListener extends GestureDetector.SimpleOnGestureListener
        implements RecyclerView.OnItemTouchListener {

    private final GestureDetector gestureDetector;
    private final ItemSelectedListener itemSelectedListener;

    public SearchAdapterTouchListener(Context context, ItemSelectedListener itemSelectedListener) {
        this.gestureDetector = new GestureDetector(context, this);
        this.itemSelectedListener = itemSelectedListener;
    }
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    public interface ItemSelectedListener {
        void itemSelected(MediaBrowserCompat.MediaItem item);
    }
}
