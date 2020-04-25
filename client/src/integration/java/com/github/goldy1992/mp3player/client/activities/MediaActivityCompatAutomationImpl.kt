package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.R

class MediaActivityCompatAutomationImpl : MediaActivityCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }

    override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_empty)
        return true
    }

    override fun logTag(): String {
        return ""
    }

    override fun initialiseDependencies() {
        super.initialiseDependencies()
        this.mediaActivityCompatComponent.inject(this)
    }
}