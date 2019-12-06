package com.github.goldy1992.mp3player.client.testsupport.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.testsupport.activities.EmptyMediaActivityCompatFragmentActivity
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.client.testsupport.dagger.modules.MockAlbumArtPainterModule
import com.github.goldy1992.mp3player.client.testsupport.dagger.modules.MockMediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.client.dagger.modules.HandlerThreadModule
import dagger.BindsInstance
import dagger.Component

@ComponentScope
@Component(modules = [ComponentNameModule::class, MockAlbumArtPainterModule::class, HandlerThreadModule::class, MainHandlerModule::class, MediaBrowserCompatModule::class, MockMediaControllerAdapterModule::class, MyDrawerListenerModule::class])
interface TestMediaActivityCompatComponent : MediaActivityCompatComponent {

    fun inject(emptyMediaActivityCompatFragmentActivity: EmptyMediaActivityCompatFragmentActivity?)
    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory {
        fun create(@BindsInstance context: Context?,
                   @BindsInstance workerId: String?,
                   @BindsInstance callback: MediaBrowserConnectorCallback?): TestMediaActivityCompatComponent?
    }
}