package com.github.goldy1992.mp3player.client.data.eventholders

import android.os.Bundle
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand

data class SessionCommandEventHolder(
    val controller: MediaController,
    val command: SessionCommand,
    val args: Bundle
)
