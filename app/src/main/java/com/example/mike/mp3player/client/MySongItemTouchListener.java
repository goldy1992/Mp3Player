package com.example.mike.mp3player.client;

import android.content.Context;
import android.view.MotionEvent;

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
            this.mediaPlayerActvityRequester.playSelectedSong(mediaId);
        }
        return false;
    }
}
