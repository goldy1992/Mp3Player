package com.github.goldy1992.mp3player

import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.endtoend.components.DaggerAutomationMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.components.DaggerAndroidTestServiceComponent
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class AutomationApplication : MainApplication() {

    override fun serviceComponent(context: Context, notificationListener: PlayerNotificationManager.NotificationListener): ServiceComponent {
        return DaggerAndroidTestServiceComponent
            .factory()
            .create(context, notificationListener, componentClassMapper)
    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectionListener): MediaActivityCompatComponent {
        return DaggerAutomationMediaActivityCompatComponent
                .factory()
                .create(context, componentClassMapper)
    }

    override fun buildComponentClassMapper(): ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .splashActivity(SplashScreenEntryActivity::class.java)
                .mainActivity(MainActivityIdlingResourceImpl::class.java)
                .folderActivity(FolderActivity::class.java)
                .service(MediaPlaybackService::class.java)
                .mediaPlayerActivity(MediaPlayerActivity::class.java)
                .searchResultActivity(SearchResultActivity::class.java)
                .build()
    }

}