package com.example.mike.mp3player.service.library.contentretriever;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.ContentRequest;
import com.example.mike.mp3player.service.library.utils.IdGenerator;

import java.util.Comparator;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;

public abstract class ContentRetriever implements Comparator<MediaBrowserCompat.MediaItem> {

    public abstract List<MediaBrowserCompat.MediaItem> getChildren(ContentRequest request);
    public abstract List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
    public abstract MediaItemType getType();
    public abstract MediaItemType getParentType();

    protected String buildMediaId(String prefix, String childItemId) {
        return new StringBuilder()
                .append(prefix)
                .append(IdGenerator.ID_SEPARATOR)
                .append(childItemId)
                .toString();
    }

    protected Bundle getExtras() {
        Bundle extras = new Bundle();
        extras.putSerializable(MEDIA_ITEM_TYPE, getType());
        return extras;
    }
}
