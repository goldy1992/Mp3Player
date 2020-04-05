package com.github.goldy1992.mp3player.client.dagger.subcomponents

import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity

import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Subcomponent

@ComponentScope
@Subcomponent
interface SplashScreenEntryActivityComponent {

    fun inject(splashScreenEntryActivity: SplashScreenEntryActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance splashScreenEntryActivity: SplashScreenEntryActivity,
                   @BindsInstance permissionGranted: PermissionGranted) : SplashScreenEntryActivityComponent
    }
}