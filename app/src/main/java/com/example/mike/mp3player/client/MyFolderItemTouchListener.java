package com.example.mike.mp3player.client;

import android.content.Context;
import android.view.MotionEvent;

import com.example.mike.mp3player.client.views.MyFolderViewAdapter;
import com.example.mike.mp3player.commons.library.Category;

public class MyFolderItemTouchListener extends MyGenericItemTouchListener {


    public MyFolderItemTouchListener(Context context) {
        super(context);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MyFolderViewAdapter myViewAdapter = (MyFolderViewAdapter) parentView.getAdapter();
            String mediaId = myViewAdapter.getFilteredSongs().get(childPosition).getDescription().getMediaId();
            this.mediaBrowserAdapter.subscribe(Category.FOLDERS, mediaId);
        }
        return false;
    }
}
