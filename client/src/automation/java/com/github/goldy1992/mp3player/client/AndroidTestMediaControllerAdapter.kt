package com.github.goldy1992.mp3player.client

import android.content.Context
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.commons.LogTagger

class AndroidTestMediaControllerAdapter

constructor(private val context: Context,
            private val myMediaControllerCallback: MyMediaControllerCallback?,
            private val awaitingMediaControllerIdlingResource: AwaitingMediaControllerIdlingResource)
    : MediaControllerAdapter(context, myMediaControllerCallback), LogTagger
{

}