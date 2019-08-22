package com.example.mike.mp3player.commons;

import android.support.v4.media.MediaDescriptionCompat;

import java.io.Serializable;

public final class MediaItemTypeInfo implements Serializable {

    private final MediaItemType mediaItemType;
    private final String typeId;

    public MediaItemTypeInfo(MediaItemType mediaItemType, String typeId) {
        this.mediaItemType = mediaItemType;
        this.typeId = typeId;
    }

    public MediaItemType getMediaItemType() {
        return mediaItemType;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getDescription() {
        return mediaItemType.getDescription();
    }
}
