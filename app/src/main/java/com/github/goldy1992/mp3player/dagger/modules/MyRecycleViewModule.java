package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener;
import com.github.goldy1992.mp3player.client.activities.FolderActivityInjector;
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat;
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivityInjector;
import com.github.goldy1992.mp3player.client.views.adapters.MyFolderViewAdapter;
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecycleViewAdapter;
import com.github.goldy1992.mp3player.client.views.adapters.MySongViewAdapter;
import com.github.goldy1992.mp3player.commons.MediaItemType;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

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
    public MyGenericRecycleViewAdapter provideRecycleViewAdapter(AlbumArtPainter albumArtPainter, MediaItemType mediaItemType, @Named("main") Handler handler) {
        switch (mediaItemType) {
            case SONGS:
            case FOLDER: return new MySongViewAdapter(albumArtPainter, handler);
            case FOLDERS: return new MyFolderViewAdapter(albumArtPainter, handler);
            default: return null;
        }
    }

    @Provides
    public MyGenericItemTouchListener provideRecycleViewTouchListener(Context context,
                    MyGenericItemTouchListener.ItemSelectedListener listener) {
        return new MyGenericItemTouchListener(context, listener);
    }
}