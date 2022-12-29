package com.github.goldy1992.mp3player.client.ui.states.eventholders

import android.os.Bundle
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionCommand.COMMAND_CODE_CUSTOM
import androidx.media3.session.SessionCommand.COMMAND_CODE_LIBRARY_GET_ITEM

data class SessionCommandEventHolder(
    val command: SessionCommand,
    val args: Bundle
) {
    companion object {
        val DEFAULT = SessionCommandEventHolder(
            SessionCommand(COMMAND_CODE_LIBRARY_GET_ITEM),
            Bundle()
        )
    }
}
