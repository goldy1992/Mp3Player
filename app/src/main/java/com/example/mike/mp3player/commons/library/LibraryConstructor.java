package com.example.mike.mp3player.commons.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.regex.PatternSyntaxException;

import static com.example.mike.mp3player.commons.Constants.FOLDER_NAME;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaId;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public class LibraryConstructor {

    private static final String LOG_TAG = "LIBRARY_CONSTRUCTOR";

    /** need to escape the character | with delimiters **/
    private static final String DELIMITER = "\\|\\|";
    private static final String LIMITER = "||";

    public static LibraryRequest parseId(String id) {
        String[] tokens = splitMediaId(id);

        if (tokens.length <= 0) {
            return null;
        }
        Category category = Category.valueOf(tokens[0]);
        String idToken = null;

        if (tokens.length >= 2) {
          idToken = tokens[1];
        }
        LibraryRequest retVal = new LibraryRequest(category, idToken);

        if (tokens.length >= 3) {
            switch (category) {
                case FOLDERS: retVal.getExtras().put(FOLDER_NAME, tokens[2]);
            }
        }

        return retVal;
    }

    public static String buildId(Category category, String mediaId) {
        if (category == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(category.name());

        if (null == mediaId) {
            return stringBuilder.toString();
        }

        stringBuilder.append(LIMITER);
        stringBuilder.append(mediaId);

        return stringBuilder.toString();
    }

    public static String buildSongId(LibraryRequest libraryRequest, String mediaId) {
        if (libraryRequest == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(libraryRequest.getCategory().name());

        if (null == mediaId) {
            return stringBuilder.toString();
        }

        /* If not a song category, add it to show we want to play songs from that category.
            e.g. FOLDERS||SONGS||REGGAETON || i.e all songs from the reggaeton */
        if (libraryRequest.getCategory() != Category.SONGS) {
            stringBuilder.append(LIMITER);
            stringBuilder.append(Category.SONGS.name());
            stringBuilder.append(LIMITER);
            stringBuilder.append(libraryRequest.getId());
        }
        stringBuilder.append(LIMITER);
        stringBuilder.append(mediaId);

        return stringBuilder.toString();
    }

    public static String getSongIdFromSongRequest(String id) {
        String[] tokens = splitMediaId(id);

        if (tokens.length <= 1) {
            return null;
        }
        return tokens[tokens.length - 1];
    }

    public static String buildId(Category category, MediaBrowserCompat.MediaItem mediaItem) {
        StringBuilder stringBuilder = new StringBuilder();
        String mediaId = getMediaId(mediaItem);

        if (mediaId == null) {
            return null;
        }
        stringBuilder.append(category);
        stringBuilder.append(LIMITER);
        stringBuilder.append(mediaId);
        if (Category.FOLDERS == category) {
            String title = getTitle(mediaItem);
            stringBuilder.append(LIMITER);
            stringBuilder.append(title);
        }
        return stringBuilder.toString();
    }

    public static Category getCategoryFromMediaItem(MediaBrowserCompat.MediaItem mediaItem) {
        if ( mediaItem == null || mediaItem.getDescription() == null) {
            return  null;
        }
        MediaDescriptionCompat description = mediaItem.getDescription();
        String id = description.getMediaId();
        String[] tokens = splitMediaId(id);
        return tokens == null || tokens.length < 1 || tokens[0] == null ?
                null : Category.valueOf(tokens[0]);
    }

    public static String[] splitMediaId(String id) {
        if (null == id) {
            return null;
        }
        String[] tokens = null;
        try {
            tokens = id.split(DELIMITER);
        } catch (PatternSyntaxException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
            return null;
        }
        return tokens;
    }

    public static void addFolderNameFromMediaItemToLibraryId(LibraryRequest libraryRequest, MediaBrowserCompat.MediaItem mediaItem) {
        if (libraryRequest != null && libraryRequest.getExtras() != null) {
            libraryRequest.getExtras().put(FOLDER_NAME, MediaItemUtils.getTitle(mediaItem));
        }
    }

}
