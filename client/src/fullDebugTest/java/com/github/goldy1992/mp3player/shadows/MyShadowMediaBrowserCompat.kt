package com.github.goldy1992.mp3player.shadows

import android.support.v4.media.MediaBrowserCompat
import org.robolectric.annotation.Implements
import org.robolectric.shadows.support.v4.ShadowMediaBrowserCompat

@Implements(MediaBrowserCompat::class)
class MyShadowMediaBrowserCompat : ShadowMediaBrowserCompat() {

}