package com.example.mike.mp3player.client.view;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.MyItemTouchListener;
import com.example.mike.mp3player.client.MyViewAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView {
    private Context context;
    private MyItemTouchListener myItemTouchListener;
    private MyViewAdapter myViewAdapter;

    public MyRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs, MediaPlayerActionListener mediaPlayerActionListener) {
        this.myViewAdapter = new MyViewAdapter(songs);
        this.setAdapter(myViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.setLayoutManager(linearLayoutManager);
        this.myItemTouchListener = new MyItemTouchListener(context);
        this.myItemTouchListener.setMediaPlayerActionListener(mediaPlayerActionListener);
        this.addOnItemTouchListener(this.getMyItemTouchListener());
        this.setItemAnimator(new DefaultItemAnimator());
        myViewAdapter.notifyDataSetChanged();
    }

    public void filter(String filterParam) {
        myViewAdapter.getFilter().filter(filterParam);
    }

    public MyItemTouchListener getMyItemTouchListener() {
        return myItemTouchListener;
    }

    public void setTouchable(boolean value) {
        myItemTouchListener.setTouchable(value);
    }
}
