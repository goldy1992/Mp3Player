package com.example.mike.mp3player.shadows;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.service.ShadowMediaSessionCompat_Token;

import org.robolectric.Shadows;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.support.v4.ShadowMediaBrowserCompat;

import static org.mockito.Mockito.mock;

@Implements(MediaBrowserCompat.class)
public class MyShadowMediaBrowserCompat extends ShadowMediaBrowserCompat {

    @NonNull
    @Implementation
    public MediaSessionCompat.Token getSessionToken() {
        return null;

    }
}
