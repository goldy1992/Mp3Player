package com.example.mike.mp3player.client;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.view.MotionEvent;
import android.view.View;

import com.example.mike.mp3player.client.views.MyFolderViewAdapter;

public class MyFolderItemTouchListener extends MyGenericItemTouchListener {

    private static final String LOG_TAG = "FLDER_ITM_TCH_LISNER";

    public MyFolderItemTouchListener(Context context, ItemSelectedListener itemSelectedListener) {
        super(context, itemSelectedListener);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        View childView = parentView.findChildViewUnder(e.getX(), e.getY());
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MyFolderViewAdapter myViewAdapter = (MyFolderViewAdapter) parentView.getAdapter();
            MediaBrowserCompat.MediaItem mediaItem = myViewAdapter.getItems().get(childPosition);
            this.itemSelectedListener.itemSelected(mediaItem);
        }
        return false;
    }
}
