package com.example.mike.mp3player.client.view;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.mike.mp3player.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private GridLayout menuItem;

    public MyViewHolder(View view) {
        super(view);
        this.menuItem = view.findViewById(R.id.menu_item);
    }


    public GridLayout getView() {
        return menuItem;
    }
}
