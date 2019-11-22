package com.github.goldy1992.mp3player.client;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.client.activities.FolderActivityInjector;
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat;
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivityInjector;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

@ComponentScope
public class IntentMapper {

    private final Map<MediaItemType, Class<? extends MediaActivityCompat>> categoryToActivityMap = new EnumMap<>(MediaItemType.class);

    private final Context context;

    @Inject
    public IntentMapper(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        // TODO: change this code to accommodate test implementations. Intents should be made in Dagger
        categoryToActivityMap.put(MediaItemType.FOLDER, MediaPlayerActivityInjector.class);
        categoryToActivityMap.put(MediaItemType.SONGS, MediaPlayerActivityInjector.class);
        categoryToActivityMap.put(MediaItemType.FOLDERS, FolderActivityInjector.class);

    }

    @Nullable
    public Intent getIntent(MediaItemType mediaItemType) {
        Class<? extends MediaActivityCompat> clazz = categoryToActivityMap.get(mediaItemType);
        if (null != clazz) {
            return new Intent(context, clazz);
        }
        return null;

    }

}
