package com.github.goldy1992.mp3player.service.library;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;

import com.github.goldy1992.mp3player.LogTagger;

import javax.inject.Inject;
import javax.inject.Named;

public class AudioObserver extends ContentObserver implements LogTagger {

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    @Inject
    public AudioObserver(@Named("worker") Handler handler) {
        super(handler);
    }

    /** {@inheritDoc} */
    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    /** {@inheritDoc} */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        onChange(selfChange, uri, -1);
    }


    /**
     * Dispatches a change notification to the observer. Includes the changed
     * content Uri when available and also the user whose content changed.
     *
     * @param selfChange True if this is a self-change notification.
     * @param uri The Uri of the changed content, or null if unknown.
     * @param userId The user whose content changed. Can be either a specific
     *         user or {@link UserHandle#USER_ALL}.
     *
     * @hide
     */
    public void onChange(boolean selfChange, Uri uri, int userId) {

        // TODO: see what type of uri is posted and either reindex the whole database or just the changed Uri

        // if a track is deleted the default MediaStore.Audio.EXTERNAL_CONTENT uri if the argument
        // If it's a new track the exact content if will appear
        // when there is a "change" to the meta data the exact id will given as the uri
        Log.i(getLogTag(),"hit on change");

    }

    @Override
    public String getLogTag() {
        return null;
    }
}
