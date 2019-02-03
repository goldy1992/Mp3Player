package com.example.mike.mp3player.commons.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.regex.PatternSyntaxException;

public class LibraryConstructor {

    private static final String LOG_TAG = "LIBRARY_CONSTRUCTOR";

    /** need to escape the character | with delimiters **/
    private static final String DELIMITER = "\\|\\|";
    private static final String LIMITER = "||";

    public static LibraryId parseId(String id) {
        String[] tokens = splitMediaId(id);

        if (tokens.length <= 0) {
            return null;
        }
        Category category = Category.valueOf(tokens[0]);
        String idToken = null;

        if (tokens.length >= 2) {
          idToken = tokens[1];
        }

        return new LibraryId(category, idToken);
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

    private static String[] splitMediaId(String id) {
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

}
