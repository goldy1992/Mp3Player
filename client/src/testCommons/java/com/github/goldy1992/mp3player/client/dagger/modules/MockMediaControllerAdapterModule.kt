package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.MockMediaControllerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [MediaControllerAdapterModule::class]
)
class MockMediaControllerAdapterModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMediaControllerAdapter(@ApplicationContext context: Context,
    mediaBrowserCompat: MediaBrowserCompat) : MediaControllerAdapter {
        return MockMediaControllerAdapter(context, mediaBrowserCompat)
    }
}