package com.example.mike.mp3player.client;

import android.content.Context;
import android.view.MotionEvent;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;

public class MySongItemTouchListener extends MyGenericItemTouchListener {

    /**
     * Here to help us know what type of request to build to the MediaController, so we know what
     * type of playlist to generate for the MediaPLayer
     */
    private Category category;

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
            this.itemSelectedListener.itemSelected(mediaId);
        }
        return false;
    }
}
