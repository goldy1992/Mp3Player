package com.example.mike.mp3player.shadows;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.support.v4.ShadowMediaBrowserCompat;

@Implements(MediaBrowserCompat.class)
public class MyShadowMediaBrowserCompat extends ShadowMediaBrowserCompat {

    @NonNull
    @Implementation
    public MediaSessionCompat.Token getSessionToken() {
        return null;

    }
}
