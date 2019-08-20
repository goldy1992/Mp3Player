package com.example.mike.mp3player.service;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.VisibleForTesting;
import androidx.media.MediaBrowserServiceCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.utils.IdGenerator;

import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import javax.inject.Named;

import static com.example.mike.mp3player.commons.Constants.PACKAGE_NAME;

public class RootAuthenticator {

    private final String acceptedMediaId;
    @VisibleForTesting
    public final String REJECTED_MEDIA_ROOT_ID = "empty_root_id";

    @Inject
    public RootAuthenticator(String acceptedMediaId) {
        this.acceptedMediaId = acceptedMediaId;
    }

    public MediaBrowserServiceCompat.BrowserRoot authenticate(String clientPackageName, int clientUid,
                                                              Bundle rootHints) {
        Bundle extras = new Bundle();
        // (Optional) Control the level of access for the specified package name.
        // You'll need to write your own logic to do this.
        if (allowBrowsing(clientPackageName, clientUid)) {
            // Returns a root ID that clients can use with onLoadChildren() to retrieve
            // the content hierarchy.
            return new MediaBrowserServiceCompat.BrowserRoot(acceptedMediaId, extras);
        } else {
            // Clients can connect, but this BrowserRoot is an empty hierachy
            // so onLoadChildren returns nothing. This disables the ability to browse for content.
            return new MediaBrowserServiceCompat.BrowserRoot(REJECTED_MEDIA_ROOT_ID, extras);
        }
    }

    public boolean allowSubscription(String id) {
        return !TextUtils.equals(REJECTED_MEDIA_ROOT_ID, id);
    }

    public boolean rejectRootSubscription(String id) {
        return REJECTED_MEDIA_ROOT_ID.equals(id);
    }

    private boolean allowBrowsing(String clientPackageName, int clientUid) {
        return clientPackageName.contains(PACKAGE_NAME);
    }

}
