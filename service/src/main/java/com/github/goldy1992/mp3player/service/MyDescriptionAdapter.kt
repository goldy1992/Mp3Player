package com.github.goldy1992.mp3player.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.Constants.MEDIA_SESSION
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MyDescriptionAdapter

    @Inject constructor(@ApplicationContext private val context: Context,
                       private val token: MediaSessionCompat.Token,
                       private val playlistManager: PlaylistManager,
                       private val componentClassMapper: ComponentClassMapper)
    : MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): String {
        return getTitle(getCurrentMediaItem(player)!!)!!
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val openUI = Intent(context, componentClassMapper.mediaPlayerActivity)
        openUI.putExtra(MEDIA_SESSION, token)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
                context, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    override fun getCurrentContentText(player: Player): String? {
        return null
    }

    override fun getCurrentLargeIcon(player: Player, callback: BitmapCallback): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, R.drawable.ic_music_note)
    }

    private fun getCurrentMediaItem(player: Player): MediaBrowserCompat.MediaItem? {
        val position = player.currentWindowIndex
        return playlistManager.getItemAtIndex(position)
    }

    companion object {
        private const val REQUEST_CODE = 501
    }

}