package com.github.goldy1992.mp3player.client.activities

import androidx.lifecycle.LifecycleObserver
import com.github.goldy1992.mp3player.client.AudioDataAdapter
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 *
 */
@ActivityRetainedScoped
class LifecycleObservers

    @Inject
    constructor(lifecycleAwareAudioChannel: AudioDataAdapter) {

    val observers: Set<LifecycleObserver>

    init {
        observers = setOf(lifecycleAwareAudioChannel)
    }
}