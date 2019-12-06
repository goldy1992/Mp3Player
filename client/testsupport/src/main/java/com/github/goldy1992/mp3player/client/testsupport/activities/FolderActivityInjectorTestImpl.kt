package com.github.goldy1992.mp3player.client.testsupport.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.activities.FolderActivity
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.testsupport.dagger.components.DaggerTestMediaActivityCompatComponent


class FolderActivityInjectorTestImpl : FolderActivity() {
    public override fun onCreate(savedInstance: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstance)
    }

    override fun initialiseDependencies() {
        val component: MediaActivityCompatComponent = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this)
        mediaActivityCompatComponent = component
        component.inject(this)
    }
}