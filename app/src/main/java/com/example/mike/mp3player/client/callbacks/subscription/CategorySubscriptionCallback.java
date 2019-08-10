package com.example.mike.mp3player.client.callbacks.subscription;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.mike.mp3player.commons.Constants.REQUEST_OBJECT;

public class CategorySubscriptionCallback extends GenericSubscriptionCallback<Category> {

    @Override
    public SubscriptionType getType() {
        return SubscriptionType.CATEGORY;
    }

    public CategorySubscriptionCallback(Handler handler) {
        super(handler);
    }

    @Override
    public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children,
                                 @NonNull Bundle options) {
        LibraryRequest libraryRequest = (LibraryRequest) options.get(REQUEST_OBJECT);
        // TODO: maybe implement logic to decide which listener the response should be sent to.
        ArrayList<MediaBrowserCompat.MediaItem> childrenArrayList = new ArrayList<>(children);
        Set<MediaBrowserResponseListener> listenersToNotify = mediaBrowserResponseListeners.get(libraryRequest.getParentType());
        if (null != listenersToNotify) {
            for (MediaBrowserResponseListener listener : listenersToNotify) {
                listener.onChildrenLoaded(parentId, childrenArrayList, options);
            }
        }
    }

}
