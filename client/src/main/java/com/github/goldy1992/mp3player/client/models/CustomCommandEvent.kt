package com.github.goldy1992.mp3player.client.models

import android.os.Bundle

data class CustomCommandEvent(
    val id : String,
    val args: Bundle
) {
    companion object {
        val DEFAULT = CustomCommandEvent(
            "0",
            Bundle()
        )
    }
}
