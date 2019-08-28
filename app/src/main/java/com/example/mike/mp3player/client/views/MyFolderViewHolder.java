package com.example.mike.mp3player.client.views;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;

public class MyFolderViewHolder extends RecyclerView.ViewHolder {

    final TextView folderName;
    final TextView folderPath;

    public MyFolderViewHolder(@NonNull View itemView) {
        super(itemView);
        this.folderName = itemView.findViewById(R.id.folderName);
        this.folderPath = itemView.findViewById(R.id.folderPath);
    }
}
