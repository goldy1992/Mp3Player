package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.TestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class EmptyMediaActivityCompatFragmentActivity : MediaActivityCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val component = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext, this, ComponentClassMapper.Builder().build())
        component.inject(this)
        mediaActivityCompatComponent = component
    }

    override fun initialiseView(layoutId: Int): Boolean {
        return true
    }

    override fun logTag(): String {
        return "EMPTY_ACTIVITY"
    }
}