package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.dagger.components.DaggerMediaActivityCompatComponent

/**
 * Media Player Activity injector
 */
class MediaPlayerActivityInjector : MediaPlayerActivity() {
    override fun onCreate(savedInstance: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstance)
    }

    public override fun initialiseDependencies() {
        val mediaActivityCompatComponent = DaggerMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this)
        mediaActivityCompatComponent.inject(this)
        this.mediaActivityCompatComponent = mediaActivityCompatComponent
    }
}