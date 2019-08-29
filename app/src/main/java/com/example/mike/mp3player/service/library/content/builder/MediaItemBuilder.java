package com.example.mike.mp3player.service.library.content.builder;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.Comparator;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;

public abstract class MediaItemBuilder implements Comparator<MediaItem> {

    //private final String idPrefix;

    public MediaItemBuilder() {
       // this.idPrefix = idPrefix;
    }

    public abstract List<MediaItem> build(Cursor cursor, String mediaIdPrefix);
    public abstract MediaItemType getType();

    protected Bundle getExtras() {
        Bundle extras = new Bundle();
        extras.putSerializable(MEDIA_ITEM_TYPE, getType());
        return extras;
    }
}
