package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.MockMediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides

@Module
class MockMediaControllerAdapterModule {

    @ComponentScope
    @Provides
    fun providesMediaControllerAdapter(context: Context, mediaControllerCallback: MyMediaControllerCallback) : MediaControllerAdapter {
        return MockMediaControllerAdapter(context, mediaControllerCallback)
    }
}