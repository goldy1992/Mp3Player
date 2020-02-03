package com.github.goldy1992.mp3player.client.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.client.activities.EmptyMediaActivityCompatFragmentActivity
import com.github.goldy1992.mp3player.client.dagger.modules.MockAlbumArtPainterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MockMediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Component

@ComponentScope
@Component(modules =
    [MockAlbumArtPainterModule::class,
    MediaBrowserCompatModule::class,
    MockMediaControllerAdapterModule::class])
interface TestMediaActivityCompatComponent : MediaActivityCompatComponent {

    fun inject(emptyMediaActivityCompatFragmentActivity: EmptyMediaActivityCompatFragmentActivity?)

    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory {

       override fun create(@BindsInstance context: Context,
                   @BindsInstance callback: MediaBrowserConnectorCallback,
                   @BindsInstance componentClassMapper: ComponentClassMapper): TestMediaActivityCompatComponent
    }
}