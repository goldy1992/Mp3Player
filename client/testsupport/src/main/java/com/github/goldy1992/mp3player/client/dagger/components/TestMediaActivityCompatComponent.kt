package com.github.goldy1992.mp3player.client.dagger.components

import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.client.activities.EmptyMediaActivityCompatFragmentActivity
import com.github.goldy1992.mp3player.client.dagger.modules.MockAlbumArtPainterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MockMediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Component

@ComponentScope
@Component(modules =
    [MockAlbumArtPainterModule::class,
    MediaBrowserCompatModule::class,
    MockMediaControllerAdapterModule::class])
interface TestMediaActivityCompatComponent : MediaActivityCompatComponent {

    open fun inject(emptyMediaActivityCompatFragmentActivity: EmptyMediaActivityCompatFragmentActivity?)

    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory {}
}