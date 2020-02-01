package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class MediaPlayerActivityInjectorTestImpl : MediaPlayerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder()
                .service(MediaPlayerActivityInjectorTestImpl::class.java)
                .build()
        val component: MediaActivityCompatComponent = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext,  this, componentClassMapper)
        mediaActivityCompatComponent = component
        component.inject(this)
    }
}