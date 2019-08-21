package com.example.mike.mp3player.commons;

import java.io.Serializable;
import java.util.Set;

public final class MediaItemLibraryInfo implements Serializable {

    private final MediaItemTypeWrapper parent;
    private final MediaItemTypeWrapper mediaItemType;
    private final Set<MediaItemTypeWrapper> childTypes;

    public MediaItemLibraryInfo(MediaItemTypeWrapper parent,
                                MediaItemTypeWrapper mediaItemType,
                                Set<MediaItemTypeWrapper> childTypes) {
        this.parent = parent;
        this.mediaItemType = mediaItemType;
        this.childTypes = childTypes;
    }

    public MediaItemTypeWrapper getParent() {
        return parent;
    }

    public MediaItemTypeWrapper getMediaItemType() {
        return mediaItemType;
    }

    public Set<MediaItemTypeWrapper> getChildTypes() {
        return childTypes;
    }
}
