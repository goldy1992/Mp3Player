package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.client.AlbumArtPainter;
import com.example.mike.mp3player.client.MyFolderItemTouchListener;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.MySongItemTouchListener;
import com.example.mike.mp3player.client.activities.FolderActivityInjector;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.activities.MediaPlayerActivityInjector;
import com.example.mike.mp3player.client.views.MyFolderViewAdapter;
import com.example.mike.mp3player.client.views.MyGenericRecycleViewAdapter;
import com.example.mike.mp3player.client.views.MySongViewAdapter;
import com.example.mike.mp3player.commons.MediaItemType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

import dagger.Module;
import dagger.Provides;

@Module
public class MyRecycleViewModule {

    private static final Map<MediaItemType, Class<? extends MediaActivityCompat>> CATEGORY_TO_ACTIVITY_MAP = new HashMap<>();
    static {
        // TODO: change this code to accommodate test implementations. Intents should be made in Dagger
        CATEGORY_TO_ACTIVITY_MAP.put(MediaItemType.FOLDER, MediaPlayerActivityInjector.class);
        CATEGORY_TO_ACTIVITY_MAP.put(MediaItemType.SONGS, MediaPlayerActivityInjector.class);
        CATEGORY_TO_ACTIVITY_MAP.put(MediaItemType.FOLDERS, FolderActivityInjector.class);
    }

    @Provides
    public Class<? extends MediaActivityCompat> provideIntentClass(MediaItemType mediaItemType) {
        return CATEGORY_TO_ACTIVITY_MAP.get(mediaItemType);
    }

    @Provides
    public MyGenericRecycleViewAdapter provideRecycleViewAdapter(AlbumArtPainter albumArtPainter, MediaItemType mediaItemType) {
        switch (mediaItemType) {
            case SONGS:
            case FOLDER: return new MySongViewAdapter(albumArtPainter);
            case FOLDERS: return new MyFolderViewAdapter(albumArtPainter);
            default: return null;
        }
    }

    @Provides
    public MyGenericItemTouchListener provideRecycleViewTouchListener(Context context,
                    MyGenericItemTouchListener.ItemSelectedListener listener, MediaItemType mediaItemType) {
        switch (mediaItemType) {
            case FOLDER:
            case SONGS: return new MySongItemTouchListener(context, listener);
            case FOLDERS: return new MyFolderItemTouchListener(context, listener);
            default: return null;
        }
    }
}
