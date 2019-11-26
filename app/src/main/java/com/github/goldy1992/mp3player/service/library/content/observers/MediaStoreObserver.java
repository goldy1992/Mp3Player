package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.github.goldy1992.mp3player.LogTagger;

public abstract class MediaStoreObserver extends ContentObserver implements LogTagger {

    private final ContentResolver contentResolver;

    public MediaStoreObserver(Handler handler, ContentResolver contentResolver) {
        super(handler);
        this.contentResolver = contentResolver;
        register();
    }

    private void register() {
        contentResolver.registerContentObserver(getUri(), true, this);
    }

    private void unregister() {
        contentResolver.unregisterContentObserver(this);
    }

    public abstract Uri getUri();
}
