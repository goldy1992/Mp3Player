package com.example.mike.mp3player.service.library;

import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

public final class ContentRequest {

    private final String fullId;
    private final String searchString;
    private final String contentRetrieverKey;

    private ContentRequest(String fullId, String searchString,
                           String contentRetrieverKey) {
        this.fullId = fullId;
        this.searchString = searchString;
        this.contentRetrieverKey = contentRetrieverKey;

    }

    public static ContentRequest parse(String id) {
        final String delimiter = "\\|";
        List<String> splitId = Arrays.asList(id.split(delimiter));
        String fullId = id;
        final int splidIdSize = splitId.size();
        if (splidIdSize == 1) {
            return new ContentRequest(fullId, id, id);
        } else if (splidIdSize >= 2) {
            fullId = TextUtils.join("|", Arrays.asList(splitId.get(0), splitId.get(1)));
            return new ContentRequest(fullId, splitId.get(1), splitId.get(0));
        }
        return null;
    }

    public String getFullId() {
        return fullId;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getContentRetrieverKey() {
        return contentRetrieverKey;
    }
}
