package com.github.goldy1992.mp3player.client.testsupport.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.testsupport.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.testsupport.dagger.components.TestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class EmptyMediaActivityCompatFragmentActivity : MediaActivityCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override val workerId: String
        get() = "WORKER_ID"

    override fun initialiseDependencies() {
        val component = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this, ComponentClassMapper.Builder().build()) as TestMediaActivityCompatComponent
        component.inject(this)
        mediaActivityCompatComponent = component
    }

    override fun initialiseView(layoutId: Int): Boolean {
        return true
    }
}