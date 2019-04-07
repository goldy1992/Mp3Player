package com.example.mike.mp3player.client.views;

import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.util.List;

import static com.example.mike.mp3player.commons.MediaItemUtils.getDescription;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public class MyFolderViewAdapter extends MyGenericRecycleViewAdapter {

    private final String LOG_TAG = "FOLDER_VIEW_ADAPTER";

    @Override
    public Category getSubscriptionCategory() {
        return Category.FOLDERS;
    }

    public MyFolderViewAdapter(MediaBrowserAdapter mediaBrowserAdapter, LibraryObject parent) {
        super(mediaBrowserAdapter, parent);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new views
        GridLayout t = (GridLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_item_menu, parent, false);

        MyViewHolder vh = new MyViewHolder(t);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Log.i(LOG_TAG, "position: " + position);
        MediaBrowserCompat.MediaItem song = getFilteredSongs().get(holder.getAdapterPosition());
        // - get element from your dataset at this position
        // - replace the contents of the views with that element
        //song.getMediaId();
        String folderName = extractFolderName(song);
        TextView folderNameText = holder.getView().findViewById(R.id.folderName);
        folderNameText.setText(folderName);

        String folderPath = extractFolderPath(song);
        TextView folderPathText = holder.getView().findViewById(R.id.folderPath);
        folderPathText.setText(folderPath);
    }

    private String extractFolderName(MediaBrowserCompat.MediaItem song) {
        return getTitle(song);
    }

    private String extractFolderPath(MediaBrowserCompat.MediaItem song) {
        return getDescription(song);
    }
}
