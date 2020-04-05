package com.github.goldy1992.mp3player.client.dagger

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent

interface ClientComponentsProvider {

    fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity,
                              permissionGranted: PermissionGranted)
            : SplashScreenEntryActivityComponent

    fun mediaActivityComponent(context: Context,
                                callback: MediaBrowserConnectorCallback)
            : MediaActivityCompatComponent


}