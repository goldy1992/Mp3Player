package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MyFolderItemTouchListener;
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

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

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

    public void initRecyclerView(Category category, MediaBrowserAdapter mediaBrowserAdapter,
                                 MediaControllerAdapter mediaControllerAdapter) {
        this.category = category;
        switch (category) {
            case SONGS:
                this.myViewAdapter = new MySongViewAdapter(mediaBrowserAdapter);
                this.myGenericItemTouchListener = new MySongItemTouchListener(context);

                break;
            case FOLDERS:
                this.myViewAdapter = new MyFolderViewAdapter(mediaBrowserAdapter);
                this.myGenericItemTouchListener = new MyFolderItemTouchListener(context);

                break;
            default: return;
        }
        this.setAdapter(myViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.setLayoutManager(linearLayoutManager);
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

}
