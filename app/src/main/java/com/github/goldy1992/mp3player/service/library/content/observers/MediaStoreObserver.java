package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.LogTagger;

public abstract class MediaStoreObserver extends ContentObserver implements LogTagger {

    private final ContentResolver contentResolver;

    public MediaStoreObserver(Handler handler, ContentResolver contentResolver) {
        super(handler);
        this.contentResolver = contentResolver;
    }

    public void register() {
        contentResolver.registerContentObserver(getUri(), true, this);
    }

    public void unregister() {
        contentResolver.unregisterContentObserver(this);
    }

    @NonNull
    public abstract Uri getUri();

    public String getUriString() {
        return getUri().toString();
    }
}
