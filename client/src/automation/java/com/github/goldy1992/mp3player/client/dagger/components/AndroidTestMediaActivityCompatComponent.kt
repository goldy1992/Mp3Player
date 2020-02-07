package com.github.goldy1992.mp3player.client.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserCompatModule
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Component

@ComponentScope
@Component(modules = [
    GlideModule::class,
    MediaBrowserCompatModule::class
])
interface AndroidTestMediaActivityCompatComponent : MediaActivityCompatComponent {

    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory {

        override fun create(@BindsInstance context: Context,
                   @BindsInstance callback : MediaBrowserConnectorCallback,
                   @BindsInstance componentClassMapper: ComponentClassMapper): AndroidTestMediaActivityCompatComponent

    }
}