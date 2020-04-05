package com.github.goldy1992.mp3player.client.dagger.subcomponents

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.activities.FolderActivity
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity
import com.github.goldy1992.mp3player.client.activities.SearchResultActivity
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaActivitySubcomponents
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserCompatModule
import com.github.goldy1992.mp3player.client.views.fragments.*
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Subcomponent

@ComponentScope
@Subcomponent(modules = [
    GlideModule::class,
    MediaActivitySubcomponents::class,
    MediaBrowserCompatModule::class
])
interface MediaActivityCompatComponent {

    /* Activities */
    fun inject(folderActivity: FolderActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(mediaPlayerActivity: MediaPlayerActivity)
    fun inject(searchResultActivity: SearchResultActivity)

    /* Subcomponents */
    fun mediaFragmentSubcomponent() : MediaFragmentSubcomponent.Factory
    fun mediaItemListFragmentSubcomponent() : MediaItemListFragmentSubcomponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance callback: MediaBrowserConnectorCallback)
                : MediaActivityCompatComponent
    }
}