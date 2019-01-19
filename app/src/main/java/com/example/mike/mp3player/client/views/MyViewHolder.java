package com.example.mike.mp3player.client.views;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;

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
