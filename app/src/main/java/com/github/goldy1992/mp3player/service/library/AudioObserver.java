package com.github.goldy1992.mp3player.service.library;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.github.goldy1992.mp3player.LogTagger;
import com.github.goldy1992.mp3player.service.library.search.managers.SearchDatabaseManagers;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A listener to the android ContentRetriever from the URI: content://media/external/audio/media or the
 * i.e. MediaStore.Audio.Media.EXTERNAL_CONTENT_URI constant.
 *
 * onChange is called when there is a change when a song indexed with the EXTERNAL_CONTENT_URI is
 * added, changed or deleted.
 */
public class AudioObserver extends ContentObserver implements LogTagger {

    /** Search Database Manager */
    private SearchDatabaseManagers searchDatabaseManagers;

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


    /** {@inheritDoc} */
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
