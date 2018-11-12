package com.example.mike.mp3player.client;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.MyViewHolder;
import com.example.mike.mp3player.commons.MetaDataKeys;

import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<MediaBrowserCompat.MediaItem> songs;
    private MyViewHolder myViewHolder;


    public MyViewAdapter(List<MediaBrowserCompat.MediaItem> songs) {
        super();
        this.songs = songs;
    }
    public void setData(List<MediaBrowserCompat.MediaItem> items) {
        if (songs == null) {
            this.songs = items;
        } else {
            songs.addAll(items);
        }
    }
        @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item_menu, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MediaBrowserCompat.MediaItem song = songs.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        CharSequence titleCharSequence = song.getDescription().getTitle();
        String title = null;
        if (null == titleCharSequence) {
            String fileName = (String) song.getDescription().getExtras().get(MetaDataKeys.META_DATA_KEY_FILE_NAME);
            if (fileName != null) {
                title = fileName;
            }
        } else {
            title = titleCharSequence.toString();
        }
        if (null != title) {
            holder.getTextView().setText(title.toString());
        }
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0: songs.size();
    }
}
