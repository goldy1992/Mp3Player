package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MyFolderItemTouchListener;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.MySongItemTouchListener;
import com.example.mike.mp3player.client.views.MyFolderViewAdapter;
import com.example.mike.mp3player.client.views.MyGenericRecycleViewAdapter;
import com.example.mike.mp3player.client.views.MySongViewAdapter;
import com.example.mike.mp3player.commons.library.Category;

import dagger.Module;
import dagger.Provides;

@Module
public class MyRecycleViewModule {

    @Provides
    public MyGenericRecycleViewAdapter provideRecycleViewAdapter(Category category, AlbumArtPainter albumArtPainter) {
        switch (category) {
            case SONGS: return new MySongViewAdapter(albumArtPainter);
            case FOLDERS: return new MyFolderViewAdapter(albumArtPainter);
            default: return null;
        }
    }

    @Provides
    public MyGenericItemTouchListener provideRecycleViewTouchListener(Context context,
                    MyGenericItemTouchListener.ItemSelectedListener listener, Category category) {
        switch (category) {
            case SONGS: return new MySongItemTouchListener(context, listener);
            case FOLDERS: return new MyFolderItemTouchListener(context, listener);
            default: return null;
        }
    }
}
