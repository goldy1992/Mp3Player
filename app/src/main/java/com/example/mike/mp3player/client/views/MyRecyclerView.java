package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.MyItemTouchListener;
import com.example.mike.mp3player.client.MySongViewAdapter;
import com.example.mike.mp3player.commons.library.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.support.v4.media.MediaBrowserCompat.*;

public class MyRecyclerView extends RecyclerView {
    private Context context;
    private MyItemTouchListener myItemTouchListener;
    private MyGenericRecycleViewAdapter myViewAdapter;
    private Category category;

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

    public void initRecyclerView(Category category, List<MediaItem> songs, MediaPlayerActionListener mediaPlayerActionListener) {
        this.category = category;

        if (category == null) {
            return;
        }

        this.myViewAdapter = new MySongViewAdapter(songs);
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

    public void disable() {
        myItemTouchListener.setEnabled(false);
    }
    public void enable() {
        myItemTouchListener.setEnabled(true);
    }

    private MyGenericRecycleViewAdapter initViewAdapter(Category category, List<MediaItem> items) {
        return new MySongViewAdapter(items);
    }
}
