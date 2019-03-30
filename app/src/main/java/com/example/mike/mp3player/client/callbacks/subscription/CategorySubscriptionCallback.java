package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

import static com.example.mike.mp3player.commons.Constants.PARENT_ID;

public class CategorySubscriptionCallback extends GenericSubscriptionCallback<Category> {

    @Override
    public SubscriptionType getType() {
        return SubscriptionType.CATEGORY;
    }

    public CategorySubscriptionCallback(MediaBrowserAdapter mediaBrowserAdapter) {
        super(mediaBrowserAdapter);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        super.onChildrenLoaded(parentId, children, options);
        LibraryRequest libraryRequest = (LibraryRequest) options.get(PARENT_ID);
        // TODO: maybe implement logic to decide which listener the response should be sent to.
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        Set<MediaBrowserResponseListener> listenersToNotify = mediaBrowserResponseListeners.get(libraryRequest.getCategory());
        if (null != listenersToNotify) {
            for (MediaBrowserResponseListener listener : listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList, options, context);
            }
        }
    }

}
