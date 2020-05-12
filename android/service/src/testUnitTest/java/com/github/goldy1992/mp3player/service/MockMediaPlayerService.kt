package com.github.goldy1992.mp3player.service

class MockMediaPlayerService : MediaPlaybackService() {
    public override fun initialiseDependencies() {
        println("hit fake service")
    }
}