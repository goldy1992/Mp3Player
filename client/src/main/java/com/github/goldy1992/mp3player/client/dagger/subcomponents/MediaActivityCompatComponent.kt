package com.github.goldy1992.mp3player.client.dagger.subcomponents

import android.content.Context
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Component

@ComponentScope
@Component(modules = [
    GlideModule::class,
    MediaBrowserAdapterModule::class,
    MediaControllerAdapterModule::class,
    MediaBrowserCompatModule::class,

    // supcomponents module
    MediaActivitySubcomponents::class
])
interface MediaActivityCompatComponent {

    /* Activities */
    fun inject(folderActivity: FolderActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(mediaPlayerActivity: MediaPlayerActivity)
    fun inject(searchResultActivity: SearchResultActivity)
    fun inject(mediaActivityCompat: MediaActivityCompat)

    /* Subcomponents */
    fun mediaFragmentSubcomponent() : MediaFragmentSubcomponent.Factory
    fun mediaItemListFragmentSubcomponent() : MediaItemListFragmentSubcomponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance componentClassMapper: ComponentClassMapper)
                : MediaActivityCompatComponent
    }
}