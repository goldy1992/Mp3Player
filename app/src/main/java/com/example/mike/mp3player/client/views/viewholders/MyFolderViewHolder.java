package com.example.mike.mp3player.client.views.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mike.mp3player.R;

public class MyFolderViewHolder extends RecyclerView.ViewHolder {

    private final TextView folderName;
    private final TextView folderPath;

    public MyFolderViewHolder(@NonNull View itemView) {
        super(itemView);
        this.folderName = itemView.findViewById(R.id.folderName);
        this.folderPath = itemView.findViewById(R.id.folderPath);
    }

    public TextView getFolderName() {
        return folderName;
    }

    public TextView getFolderPath() {
        return folderPath;
    }
}
