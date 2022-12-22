package com.github.goldy1992.mp3player.client

import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.test.utils.

class MockMediaController

    constructor(context: Context, sessionToken: SessionToken)
    : MediaController(context, sessionToken, ) {

    init {
        MediaController.Builder(context, sessionToken)
            .setListener()
            .buildAsync()
    }
    private val context : Context
}