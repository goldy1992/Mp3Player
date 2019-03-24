package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

public class NotifyAllSubscriptionCallback extends GenericSubscriptionCallback {
    public NotifyAllSubscriptionCallback(MediaBrowserAdapter mediaBrowserAdapter) {
        super(mediaBrowserAdapter);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);
        LibraryId libraryId = (LibraryId) options.get(PARENT_ID);

        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        for (Category c : mediaBrowserResponseListeners.keySet()) {
            for (MediaBrowserResponseListener m : mediaBrowserResponseListeners.get(c)) {
                m.onChildrenLoaded(parentId, childrenArrayList, options, context);
            }
        }
    }
}
