package com.github.goldy1992.mp3player.client.dagger.components

import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.BindsInstance
import dagger.Component

@Component
interface SplashScreenEntryActivityComponent {
    fun inject(splashScreenEntryActivity: SplashScreenEntryActivity)
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance splashScreenEntryActivity: SplashScreenEntryActivity,
                   @BindsInstance permissionGranted: PermissionGranted,
                   @BindsInstance componentClassMapper: ComponentClassMapper): SplashScreenEntryActivityComponent
    }
}