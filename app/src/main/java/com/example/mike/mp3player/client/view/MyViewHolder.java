package com.example.mike.mp3player.client.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mike.mp3player.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView title;

    public MyViewHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.title);
    }

    public TextView getTextView() {
        return title;
    }
}
