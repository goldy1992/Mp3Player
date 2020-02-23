package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.client.AndroidTestMediaControllerAdapter
import com.github.goldy1992.mp3player.client.AwaitingMediaControllerIdlingResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides

@Module
class AndroidTestMediaControllerModule {

    @Provides
    @ComponentScope
    fun providesMediaControllerAdapter(context: Context,
                                       myMediaControllerCallback: MyMediaControllerCallback,
                                       awaitingMediaControllerIdlingResource: AwaitingMediaControllerIdlingResource) : MediaControllerAdapter {
        return AndroidTestMediaControllerAdapter(context, myMediaControllerCallback, awaitingMediaControllerIdlingResource)
    }

    @Provides
    @ComponentScope
    fun providesMediaControllerCallback(myPlaybackStateCallback: MyPlaybackStateCallback,
                                        myMetadataCallback: MyMetadataCallback,
                                        awaitingMediaControllerIdlingResource: AwaitingMediaControllerIdlingResource) {

    }
}