package com.github.goldy1992.mp3player.service.library.content.observers

import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MediaStoreObservers @Inject constructor(audioObserver: AudioObserver) {
    private val mediaStoreObserversList: MutableList<MediaStoreObserver>
    fun init(mediaLibrarySession: MediaLibrarySession) {
        for (mediaStoreObserver in mediaStoreObserversList) {
            mediaStoreObserver.init(mediaLibrarySession)
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