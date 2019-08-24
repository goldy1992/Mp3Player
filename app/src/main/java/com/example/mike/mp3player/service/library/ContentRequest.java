package com.example.mike.mp3player.service.library;

import java.util.Arrays;
import java.util.List;

public final class ContentRequest {

    private final String fullId;
    private final String searchString;
    private final String contentRetrieverKey;

    private ContentRequest(String fullId, String searchString, String contentRetrieverKey) {
        this.fullId = fullId;
        this.searchString = searchString;
        this.contentRetrieverKey = contentRetrieverKey;
    }

    public static ContentRequest parse(String id) {
        List<String> splitId = Arrays.asList(id.split("\\|"));
        switch (splitId.size())
        {
            case 1: return new ContentRequest(id, id, id);
            case 2:
            case 3: return new ContentRequest(id, splitId.get(1), splitId.get(0));
            default: return null;
        }
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
