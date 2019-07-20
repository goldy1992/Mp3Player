package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mike.mp3player.client.MyFolderItemTouchListener;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.MySongItemTouchListener;
import com.example.mike.mp3player.client.MySongViewAdapter;
import com.example.mike.mp3player.commons.library.Category;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

public class MyRecyclerView extends FastScrollRecyclerView {
    private final Context context;
    private final LinearLayoutManager linearLayoutManager;
    private final DefaultItemAnimator defaultItemAnimator;

    private MyGenericRecycleViewAdapter myViewAdapter;
    private MyGenericItemTouchListener myGenericItemTouchListener;
    public MyRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.linearLayoutManager = new LinearLayoutManager(context);
        this.defaultItemAnimator = new DefaultItemAnimator();
    }

    /**
     * @param category a category for the type of adapter to use
     * @param itemSelectedListener the Item Listener callback object reference
     */
    public void initRecyclerView(Category category,
                                 MyGenericItemTouchListener.ItemSelectedListener itemSelectedListener) {
        setAdapterAndListener(category, itemSelectedListener);
        this.setLayoutManager(linearLayoutManager);
        this.addOnItemTouchListener(this.getMyGenericItemTouchListener());
        this.setItemAnimator(new DefaultItemAnimator());
        this.myViewAdapter.notifyDataSetChanged();
    }

    public MyGenericItemTouchListener getMyGenericItemTouchListener() {
        return myGenericItemTouchListener;
    }

    private void setAdapterAndListener(Category category,  MyGenericItemTouchListener.ItemSelectedListener itemSelectedListener) {
        switch (category) {
            case SONGS:
                this.myViewAdapter = new MySongViewAdapter();
                this.myGenericItemTouchListener = new MySongItemTouchListener(context, itemSelectedListener);
                break;
            case FOLDERS:
                this.myViewAdapter = new MyFolderViewAdapter();
                this.myGenericItemTouchListener = new MyFolderItemTouchListener(context, itemSelectedListener);
                break;
            default: return;
        }
        this.setAdapter(this.myViewAdapter);
    }

    public MyGenericRecycleViewAdapter getMyViewAdapter() {
        return myViewAdapter;
    }
}
