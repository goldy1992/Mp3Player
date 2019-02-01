package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.MySongItemTouchListener;
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
    private MyGenericItemTouchListener myGenericItemTouchListener;
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

        switch (category) {
            case SONGS:  myViewAdapter = new MySongViewAdapter(songs); break;
            case FOLDERS: myViewAdapter = new MyFolderViewAdapter(songs); break;
            default: return;
        }

        this.setAdapter(myViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.setLayoutManager(linearLayoutManager);
        this.myGenericItemTouchListener = new MySongItemTouchListener(context);
        this.myGenericItemTouchListener.setMediaPlayerActionListener(mediaPlayerActionListener);
        this.addOnItemTouchListener(this.getMyGenericItemTouchListener());
        this.setItemAnimator(new DefaultItemAnimator());
        myViewAdapter.notifyDataSetChanged();
    }

    public void filter(String filterParam) {
        myViewAdapter.getFilter().filter(filterParam);
    }

    public MyGenericItemTouchListener getMyGenericItemTouchListener() {
        return myGenericItemTouchListener;
    }

    public void disable() {
        myGenericItemTouchListener.setEnabled(false);
    }
    public void enable() {
        myGenericItemTouchListener.setEnabled(true);
    }

    private MyGenericRecycleViewAdapter initViewAdapter(Category category, List<MediaItem> items) {
        return new MySongViewAdapter(items);
    }
}
