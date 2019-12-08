package com.github.goldy1992.mp3player.service.shadows

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.support.v4.ShadowMediaBrowserCompat

@Implements(MediaBrowserCompat::class)
class MyShadowMediaBrowserCompat : ShadowMediaBrowserCompat() {
    @get:Implementation
    val sessionToken: MediaSessionCompat.Token
        get() = null
}