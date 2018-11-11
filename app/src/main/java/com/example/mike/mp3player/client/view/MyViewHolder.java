package com.example.mike.mp3player.client.view;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;

    public MyViewHolder(TextView textView) {
        super(textView);
        this.textView = textView;
    }


    public TextView getTextView() {
        return textView;
    }
}
