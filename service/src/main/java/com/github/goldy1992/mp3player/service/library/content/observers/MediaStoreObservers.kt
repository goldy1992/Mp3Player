package com.github.goldy1992.mp3player.service.library.content.observers

import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ComponentScope
class MediaStoreObservers @Inject constructor(audioObserver: AudioObserver) {
    private val mediaStoreObserversList: MutableList<MediaStoreObserver>
    fun init(mediaPlaybackService: MediaPlaybackService?) {
        for (mediaStoreObserver in mediaStoreObserversList) {
            mediaStoreObserver.init(mediaPlaybackService)
        }
        registerAll()
    }

    fun registerAll() {
        for (mediaStoreObserver in mediaStoreObserversList) {
            mediaStoreObserver.register()
        }
    }

    fun unregisterAll() {
        for (mediaStoreObserver in mediaStoreObserversList) {
            mediaStoreObserver.unregister()
        }
    }

    init {
        mediaStoreObserversList = ArrayList()
        mediaStoreObserversList.add(audioObserver)
    }
}