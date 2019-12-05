package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent

class SearchResultActivityInjectorTestImpl : SearchResultActivity() {
    public override fun onCreate(savedInstance: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstance)
    }

    public override fun initialiseDependencies() {
        val component: MediaActivityCompatComponent = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this)
        mediaActivityCompatComponent = component
        component.searchResultActivitySubComponent().inject(this)
    }
}