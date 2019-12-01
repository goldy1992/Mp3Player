package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.dagger.components.DaggerMediaActivityCompatComponent

class MainActivityInjector : MainActivity() {
    public override fun onCreate(savedInstance: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstance)
    }

    /** {@inheritDoc}  */
    public override fun initialiseDependencies() {
        val mediaActivityCompatComponent = DaggerMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this)
        this.mediaActivityCompatComponent = mediaActivityCompatComponent
        mediaActivityCompatComponent.inject(this)
    }
}