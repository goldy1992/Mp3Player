package com.github.goldy1992.mp3player.service.player

import android.os.Bundle
import android.util.Log
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.commons.Constants.AUDIO_SESSION_ID
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MyAnalyticsListener
    @Inject
    constructor(private val mediaSession : MediaSession) : AnalyticsListener, LogTagger {


    override fun onAudioSessionIdChanged(
        eventTime: AnalyticsListener.EventTime,
        audioSessionId: Int
    ) {
        Log.i(logTag(), "new AudioSessionId: $audioSessionId")
        super.onAudioSessionIdChanged(eventTime, audioSessionId)
        val toSend = Bundle()
        toSend.putInt(AUDIO_SESSION_ID, audioSessionId)
        val sessionCommand = SessionCommand(AUDIO_SESSION_ID, toSend)
        mediaSession.broadcastCustomCommand(sessionCommand, Bundle())
    }

    override fun logTag(): String {
        return "MyAnalyticsListener"
    }

    init {
        Log.d(logTag(), "initialised")
    }
}