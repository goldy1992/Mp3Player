package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.LogTagger;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.managers.FolderDatabaseManager;
import com.github.goldy1992.mp3player.service.library.search.managers.SongDatabaseManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * A listener to the android ContentRetriever from the URI: content://media/external/audio/media or the
 * i.e. MediaStore.Audio.Media.EXTERNAL_CONTENT_URI constant.
 *
 * onChange is called when there is a change when a song indexed with the EXTERNAL_CONTENT_URI is
 * added, changed or deleted, and update respective song and folder lists
 */
@Singleton
public class AudioObserver extends MediaStoreObserver implements LogTagger {

    /** Search Database Manager */
    private SongDatabaseManager songDatabaseManager;

    /** Search Database Manager */
    private FolderDatabaseManager folderDatabaseManager;
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
                         ContentManager contentManager,
                         SongDatabaseManager songDatabaseManager,
                         FolderDatabaseManager folderDatabaseManager,
                         MediaItemTypeIds mediaItemTypeIds) {
        super(handler, contentResolver, mediaItemTypeIds);
        this.songDatabaseManager = songDatabaseManager;
        this.folderDatabaseManager = folderDatabaseManager;
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


    /**
     * {@inheritDoc}
     *
     * For the purpose of the Normalised search database we're not interested in who made the change
     * therefore parameter, selfChange and userId are not used.
     *
     * @param selfChange not used
     * @param uri the uri that has changed
     * @param userId not used
     *
     */
    public void onChange(boolean selfChange, Uri uri, int userId) {
        if (startsWithUri(uri)) {
            updateSearchDatabase(uri);
            mediaPlaybackService.notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.SONGS));
            mediaPlaybackService.notifyChildrenChanged(mediaItemTypeIds.getId(MediaItemType.FOLDERS));
        }
        // when there is a "change" to the meta data the exact id will given as the uri
        Log.i(logTag(),"hit on change");
    }

    private void updateSearchDatabase(Uri uri) {
        long id = INVALID_ID;
        try {
            id = ContentUris.parseId(uri);
        } catch (Exception ex) {
            Log.e(logTag(), ExceptionUtils.getStackTrace(ex));
        }

        // If we know the id then just get that id
        if (INVALID_ID != id) {
            MediaItem result = contentManager.getItem(id);
            if (null != result) {
                Log.i(logTag(), "UPDATING songs and folders index");
                songDatabaseManager.insert(result);
                folderDatabaseManager.insert(result);
                Log.i(logTag(), "UPDATED songs and folders");
                String directoryPath = MediaItemUtils.getDirectoryPath(result);
                if (StringUtils.isNotEmpty(directoryPath)) {
                    mediaPlaybackService.notifyChildrenChanged(directoryPath);
                }
            }
        } else {
            songDatabaseManager.reindex();
            folderDatabaseManager.reindex();
        }
    }

    @Override
    public String getLogTag() {
        return "AUDIO_OBSERVER";
    }

    @Override
    @NonNull
    public Uri getUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }


}
