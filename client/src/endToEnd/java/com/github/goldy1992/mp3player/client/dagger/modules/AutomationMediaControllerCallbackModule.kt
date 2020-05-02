package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.callbacks.AndroidTestMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback
import com.github.goldy1992.mp3player.client.callbacks.queue.MyQueueCallback
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides

@Module
class AutomationMediaControllerCallbackModule {

    @Provides
    @ComponentScope
    fun providesMediaControllerCallback(myPlaybackStateCallback: MyPlaybackStateCallback,
                                        myMetadataCallback: MyMetadataCallback,
                                        myQueueCallback: MyQueueCallback)
            : MyMediaControllerCallback {
        return AndroidTestMediaControllerCallback(myMetadataCallback, myPlaybackStateCallback, myQueueCallback)
    }
}