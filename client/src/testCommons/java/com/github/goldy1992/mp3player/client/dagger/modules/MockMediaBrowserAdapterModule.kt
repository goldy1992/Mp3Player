package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MockMediaBrowserAdapter
import com.github.goldy1992.mp3player.client.callbacks.search.MySearchCallback
import com.github.goldy1992.mp3player.client.callbacks.subscription.MediaIdSubscriptionCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [MediaBrowserAdapterModule::class]
)
class MockMediaBrowserAdapterModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMockMediaBrowserAdapter(mediaIdSubscriptionCallback: MediaIdSubscriptionCallback,
                                       mySearchCallback: MySearchCallback) : MediaBrowserAdapter {
        return MockMediaBrowserAdapter(mediaIdSubscriptionCallback, mySearchCallback)
    }
}