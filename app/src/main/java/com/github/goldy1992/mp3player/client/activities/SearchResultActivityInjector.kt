package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.DaggerMediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.MikesMp3Player

class SearchResultActivityInjector : SearchResultActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    public override fun initialiseDependencies() {
        var app : MikesMp3Player = applicationContext!! as MikesMp3Player
        val component = DaggerMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this, app.getComponentClassMapper())
        mediaActivityCompatComponent = component
        component.searchResultActivitySubComponent().inject(this)
    }
}