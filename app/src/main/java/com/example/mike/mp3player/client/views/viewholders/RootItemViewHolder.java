package com.example.mike.mp3player.client.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;

public class RootItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;

    public RootItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.title);
    }

    public TextView getTitle() {
        return title;
    }
}
