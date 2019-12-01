package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.LogTagger;
import com.github.goldy1992.mp3player.service.MediaPlaybackService;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;

public abstract class MediaStoreObserver extends ContentObserver implements LogTagger {

    private final ContentResolver contentResolver;

    final MediaItemTypeIds mediaItemTypeIds;

    MediaPlaybackService mediaPlaybackService;

    public MediaStoreObserver(Handler handler,
                              ContentResolver contentResolver,
                              MediaItemTypeIds mediaItemTypeIds) {
        super(handler);
        this.contentResolver = contentResolver;
        this.mediaItemTypeIds = mediaItemTypeIds;
    }

    public void init(MediaPlaybackService mediaPlaybackService) {
        this.mediaPlaybackService = mediaPlaybackService;
    }

    public void register() {
        contentResolver.registerContentObserver(getUri(), true, this);
    }

    public void unregister() {
        contentResolver.unregisterContentObserver(this);
    }

    boolean startsWithUri(Uri uri) {
        return uri != null && uri.toString().startsWith(getUriString());
    }

    @NonNull
    public abstract Uri getUri();

    @NonNull
    public String getUriString() {
        return getUri().toString();
    }
}
