package com.github.goldy1992.mp3player

import android.app.Application
import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.ClientComponentsProvider
import com.github.goldy1992.mp3player.client.dagger.components.DaggerIntegrationMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.DaggerIntegrationTestAppComponent
import com.github.goldy1992.mp3player.client.dagger.components.IntegrationTestAppComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.DaggerSplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class IntegrationTestApplication : Application(),
        ClientComponentsProvider {

    private lateinit var appComponent: IntegrationTestAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerIntegrationTestAppComponent
                .factory()
                .create(componentClassMapper)
        appComponent.inject(this)
    }

    private val componentClassMapper: ComponentClassMapper =
            ComponentClassMapper.Builder()
                    .splashActivity(SplashScreenEntryActivity::class.java)
                    .mainActivity(MainActivity::class.java)
                    .folderActivity(FolderActivity::class.java)
                    .mediaPlayerActivity(MediaPlayerActivity::class.java)
                    .searchResultActivity(SearchResultActivity::class.java)
                    .build()

    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity, permissionGranted: PermissionGranted): SplashScreenEntryActivityComponent {
       return DaggerSplashScreenEntryActivityComponent
               .factory()
               .create(splashScreenEntryActivity, permissionGranted, componentClassMapper)
    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectionListener): MediaActivityCompatComponent {
        return DaggerIntegrationMediaActivityCompatComponent
                .factory()
                .create(context, componentClassMapper)
    }
}