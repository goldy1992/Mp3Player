package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.R

class MediaActivityCompatAutomationImpl : MediaActivityCompat(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }

    override fun initialiseView(layoutId: Int): Boolean {
        setContentView(layoutId)
        return true
    }

    override fun logTag(): String {
        TODO("Not yet implemented")
    }

    override fun initialiseDependencies() {
        super.initialiseDependencies()
        this.mediaActivityCompatComponent.inject(this)
    }
}