package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback
import com.github.goldy1992.mp3player.client.MockMediaControllerAdapter
import dagger.Module
import dagger.Provides

@Module
class MockMediaControllerAdapterModule {
    @Provides
    fun provideMediaControllerAdapter(context: Context?, myMediaControllerCallback: MyMediaControllerCallback): MediaControllerAdapter {
        return MockMediaControllerAdapter(context, myMediaControllerCallback)
    }
}