package com.example.mike.mp3player.client;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.example.mike.mp3player.client.views.MySongViewAdapter;

public class MySongItemTouchListener extends MyGenericItemTouchListener {

    public MySongItemTouchListener(Context context, ItemSelectedListener itemSelectedListener) {
        super(context, itemSelectedListener);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        super.onSingleTapConfirmed(e);
        View childView = parentView.findChildViewUnder(e.getX(), e.getY());
        if (null != childView) {
            int childPosition = parentView.getChildAdapterPosition(childView);
            MySongViewAdapter myViewAdapter = (MySongViewAdapter) parentView.getAdapter();
            if (null != myViewAdapter) {
                this.itemSelectedListener.itemSelected(myViewAdapter.getItems().get(childPosition));
            }
        }
        return false;
    }
}
