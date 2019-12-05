package com.github.goldy1992.mp3player.client.activities

import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector

class SplashScreenEntryActivityInjector : SplashScreenEntryActivity() {

    override fun mainActivityClass(): Class<*> {
        return MainActivityInjector::class.java
    }

    override fun serviceClass(): Class<*> {
        return MediaPlaybackServiceInjector::class.java
    }
}