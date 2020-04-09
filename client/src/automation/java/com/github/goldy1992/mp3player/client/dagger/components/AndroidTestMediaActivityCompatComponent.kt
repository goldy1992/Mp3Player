package com.github.goldy1992.mp3player.client.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.MockMediaControllerAdapter
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompatAutomationImpl
import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Subcomponent

@ComponentScope
@Subcomponent(modules = [
    AndroidTestMediaControllerModule::class,
    GlideModule::class,
    MockMediaBrowserAdapterModule::class,
    MockMediaControllerAdapterModule::class
])
interface AndroidTestMediaActivityCompatComponent : MediaActivityCompatComponent {


    @Subcomponent.Factory
    interface Factory : MediaActivityCompatComponent.Factory {

       override fun create(@BindsInstance context: Context,
                   @BindsInstance callback : MediaBrowserConnectorCallback)
                : MediaActivityCompatComponent

    }
}