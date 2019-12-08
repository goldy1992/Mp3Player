package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper


class FolderActivityInjectorTestImpl : FolderActivity() {
    public override fun onCreate(savedInstance: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstance)
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
                .create(applicationContext, workerId, this, componentClassMapper)
        mediaActivityCompatComponent = component
        component.inject(this)
    }
}