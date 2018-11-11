package com.example.mike.mp3player.client;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.mike.mp3player.client.view.MyViewHolder;

import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<MediaBrowserCompat.MediaItem> songs;
    private MyViewHolder myViewHolder;


    public MyViewAdapter(List<MediaBrowserCompat.MediaItem> songs) {
        super();
        this.songs = songs;
    }
    public void setData(List<MediaBrowserCompat.MediaItem> items) {
        this.songs = items;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
