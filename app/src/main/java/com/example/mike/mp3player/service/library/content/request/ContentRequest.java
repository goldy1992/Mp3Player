package com.example.mike.mp3player.service.library.content.request;

import androidx.annotation.VisibleForTesting;

import javax.annotation.Nullable;

public final class ContentRequest {

    private final String queryString;
    private final String contentRetrieverKey;
    @Nullable
    private String mediaIdPrefix;

    @VisibleForTesting
    public ContentRequest(String queryString,
                           String contentRetrieverKey,
                           String mediaIdPrefix) {
        this.queryString = queryString;
        this.contentRetrieverKey = contentRetrieverKey;
        this.mediaIdPrefix = mediaIdPrefix;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getContentRetrieverKey() {
        return contentRetrieverKey;
    }

    public String getMediaIdPrefix() {
        return mediaIdPrefix;
    }
}
