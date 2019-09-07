package com.example.mike.mp3player.client;

import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;

public class MySearchSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = "com.example.mike.mp3player.client.MySearchSuggestionsProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public MySearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // gets recent suggestions
        Cursor recentSuggestions = super.query(uri, projection, selection, selectionArgs, sortOrder);

        // get string query
        String query = uri.getLastPathSegment().toLowerCase();
        return null;
    }
}
