package com.example.mike.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.Comparator;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;

public abstract class ResultsParser implements Comparator<MediaItem> {

    public abstract List<MediaItem> create(@NonNull Cursor cursor, String mediaIdPrefix);
    public abstract MediaItemType getType();

    protected Bundle getExtras() {
        Bundle extras = new Bundle();
        extras.putSerializable(MEDIA_ITEM_TYPE, getType());
        return extras;
    }
}
