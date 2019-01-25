package com.example.mike.mp3player.commons.library;

import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.regex.PatternSyntaxException;

public class LibraryConstructor {

    private static final String LOG_TAG = "LIBRARY_CONSTRUCTOR";

    public static final String DELIMITER = "||";

    public static LibraryId parseId(String id) {
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

        stringBuilder.append(DELIMITER);
        stringBuilder.append(mediaId);

        return stringBuilder.toString();
    }

}
