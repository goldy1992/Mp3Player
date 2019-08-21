package com.example.mike.mp3player.commons;

import java.io.Serializable;

public final class MediaItemTypeWrapper implements Serializable {

    private final MediaItemType mediaItemType;
    private final String typeId;

    public MediaItemTypeWrapper(MediaItemType mediaItemType, String typeId) {
        this.mediaItemType = mediaItemType;
        this.typeId = typeId;
    }

    public MediaItemType getMediaItemType() {
        return mediaItemType;
    }

    public String getTypeId() {
        return typeId;
    }
}
