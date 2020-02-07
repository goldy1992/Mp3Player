package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper


class FolderActivityInjectorTestImpl : FolderActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val componentClassMapper: ComponentClassMapper = ComponentClassMapper.Builder()
            .service(FolderActivityInjectorTestImpl::class.java)
            .folderActivity(FolderActivityInjectorTestImpl::class.java)
            .mainActivity(TestMainActivity::class.java)
            .mediaPlayerActivity(MediaPlayerActivityInjectorTestImpl::class.java)
            .build()

        val component: MediaActivityCompatComponent = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext,  this, componentClassMapper)
        mediaActivityCompatComponent = component
        component.inject(this)
    }
}