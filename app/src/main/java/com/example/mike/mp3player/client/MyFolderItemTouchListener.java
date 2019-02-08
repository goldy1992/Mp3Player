package com.example.mike.mp3player.client;

import android.content.Context;
import android.media.browse.MediaBrowser;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mike.mp3player.client.views.MyFolderViewAdapter;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;

public class MyFolderItemTouchListener extends MyGenericItemTouchListener {

    private static final String LOG_TAG = "FLDER_ITM_TCH_LISNER";

    public MyFolderItemTouchListener(Context context) {
        super(context);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MyFolderViewAdapter myViewAdapter = (MyFolderViewAdapter) parentView.getAdapter();
            MediaBrowserCompat.MediaItem mediaItem = myViewAdapter.getFilteredSongs().get(childPosition);
            String mediaId = LibraryConstructor.buildId(Category.FOLDERS, mediaItem);
//            Log.i(LOG_TAG, "id: " + mediaId + " | was clicked");
            this.itemSelectedListener.itemSelected(mediaId);
        }
        return false;
    }
}
