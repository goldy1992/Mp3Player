package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;

import com.github.goldy1992.mp3player.LogTagger;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.search.managers.SearchDatabaseManagers;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * A listener to the android ContentRetriever from the URI: content://media/external/audio/media or the
 * i.e. MediaStore.Audio.Media.EXTERNAL_CONTENT_URI constant.
 *
 * onChange is called when there is a change when a song indexed with the EXTERNAL_CONTENT_URI is
 * added, changed or deleted.
 */
@Singleton
public class AudioObserver extends MediaStoreObserver implements LogTagger {

    /** Search Database Manager */
    private SearchDatabaseManagers searchDatabaseManagers;
    /** Content manager */
    private ContentManager contentManager;

    private static final long INVALID_ID = -1;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    @Inject
    public AudioObserver(@Named("worker") Handler handler,
                         ContentResolver contentResolver,
                         SearchDatabaseManagers searchDatabaseManagers,
                         ContentManager contentManager) {
        super(handler, contentResolver);
        this.searchDatabaseManagers = searchDatabaseManagers;
        this.contentManager = contentManager;
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

        if (null != uri) {

            // IF son meta data has changed
            if (uri.toString().startsWith(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString())) {
                long id = -1;

                try {
                    id = ContentUris.parseId(uri);
                    if (INVALID_ID != id) {
                        MediaItem result = contentManager.getItem(id);
                        Log.i(getLogTag(), "");
                    }

                } catch (Exception ex) {
                    Log.e(getLogTag(), ExceptionUtils.getStackTrace(ex));
                    return;
                }
            }



        }
        // if a track is deleted the default MediaStore.Audio.EXTERNAL_CONTENT uri if the argument
        // If it's a new track the exact content if will appear
        // when there is a "change" to the meta data the exact id will given as the uri
        Log.i(getLogTag(),"hit on change");

    }

    @Override
    public String getLogTag() {
        return "AUDIO_OBSERVER";
    }

    @Override
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }
}
