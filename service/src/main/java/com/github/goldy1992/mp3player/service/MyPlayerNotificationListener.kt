package com.github.goldy1992.mp3player.service

import android.app.Notification
import android.app.Service
import androidx.media3.ui.PlayerNotificationManager
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MyPlayerNotificationListener


    @Inject
    constructor(private val serviceParam: Service) : PlayerNotificationManager.NotificationListener {

    private val service: MediaPlaybackService = serviceParam as MediaPlaybackService

    /**
     * Called each time after the notification has been posted.
     *
     *
     * For a service, the `ongoing` flag can be used as an indicator as to whether it
     * should be in the foreground.
     *
     * @param notificationId The id of the notification which has been posted.
     * @param notification The [Notification].
     * @param ongoing Whether the notification is ongoing.
     */
    override fun onNotificationPosted(notificationId: Int,
                                      notification: Notification,
                                      ongoing: Boolean) { // fix to make notifications removable
        if (!ongoing) {
            service.stopForeground(false)
        } else {
            service.startForeground(notificationId, notification)
        }
    }
}