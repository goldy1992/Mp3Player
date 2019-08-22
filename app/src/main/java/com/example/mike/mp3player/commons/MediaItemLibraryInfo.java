package com.example.mike.mp3player.commons;

import java.io.Serializable;
import java.util.Set;

public final class MediaItemLibraryInfo implements Serializable {

    private final MediaItemTypeInfo parent;
    private final MediaItemTypeInfo mediaItemType;
    private final Set<MediaItemTypeInfo> childTypes;

    public MediaItemLibraryInfo(MediaItemTypeInfo parent,
                                MediaItemTypeInfo mediaItemType,
                                Set<MediaItemTypeInfo> childTypes) {
        this.parent = parent;
        this.mediaItemType = mediaItemType;
        this.childTypes = childTypes;
    }

    public MediaItemTypeInfo getParent() {
        return parent;
    }

    public MediaItemTypeInfo getMediaItemType() {
        return mediaItemType;
    }

    public Set<MediaItemTypeInfo> getChildTypes() {
        return childTypes;
    }
}
