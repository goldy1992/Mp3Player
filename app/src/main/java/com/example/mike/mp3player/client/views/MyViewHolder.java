package com.example.mike.mp3player.client.views;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private ViewGroup menuItem;

    public MyViewHolder(View view) {
        super(view);
        this.menuItem = view.findViewById(R.id.menu_item);
    }

    public ViewGroup getView() {
        return menuItem;
    }
}
