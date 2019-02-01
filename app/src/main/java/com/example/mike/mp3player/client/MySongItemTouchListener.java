package com.example.mike.mp3player.client;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.mike.mp3player.client.views.MediaPlayerActionListener;
import com.example.mike.mp3player.client.views.MyRecyclerView;

public class MySongItemTouchListener extends MyGenericItemTouchListener {


    public MySongItemTouchListener(Context context) {
        super(context);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MySongViewAdapter myViewAdapter = (MySongViewAdapter) parentView.getAdapter();
            String mediaId = myViewAdapter.getFilteredSongs().get(childPosition).getDescription().getMediaId();
            this.mediaPlayerActionListener.playSelectedSong(mediaId);
        }
        return false;
    }
}
