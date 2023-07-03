package com.github.goldy1992.mp3player.client.data

import android.os.Bundle
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionCommand.COMMAND_CODE_LIBRARY_GET_ITEM

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
