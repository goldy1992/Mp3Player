package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.dagger.components.DaggerMediaActivityCompatComponent

class SearchResultActivityInjector : SearchResultActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    public override fun initialiseDependencies() {
        val component = DaggerMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this)
        mediaActivityCompatComponent = component
        component.searchResultActivitySubComponent().inject(this)
    }
}