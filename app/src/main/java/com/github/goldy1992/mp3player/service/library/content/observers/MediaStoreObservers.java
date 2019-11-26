package com.github.goldy1992.mp3player.service.library.content.observers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaStoreObservers {

    private List<MediaStoreObserver> mediaStoreObserversList;

    @Inject
    public MediaStoreObservers(AudioObserver audioObserver) {
        this.mediaStoreObserversList = new ArrayList<>();
        this.mediaStoreObserversList.add(audioObserver);
    }

    public void registerAll() {
        for (MediaStoreObserver mediaStoreObserver : mediaStoreObserversList) {
            mediaStoreObserver.register();
        }
    }

    public void unregisterAll() {
        for (MediaStoreObserver mediaStoreObserver : mediaStoreObserversList) {
            mediaStoreObserver.unregister();
        }
    }
}
