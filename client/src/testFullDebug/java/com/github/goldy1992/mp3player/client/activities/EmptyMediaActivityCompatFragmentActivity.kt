package com.github.goldy1992.mp3player.client.activities

class EmptyMediaActivityCompatFragmentActivity : MediaActivityCompat() {


    override fun initialiseView(): Boolean {
        return true
    }

    override fun logTag(): String {
        return "EMPTY_ACTIVITY"
    }
}