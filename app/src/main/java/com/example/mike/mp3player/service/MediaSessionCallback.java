package com.example.mike.mp3player.service;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.unusedClasses.MediaNotificationManager;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private MediaSessionCompat mediaSession;
    private Context mContext;
    private MediaPlaybackService service;
    private MyMediaPlayerAdapter myMediaPlayerAdapter;
    private MediaNotificationManager mMediaNotificationManager;
    private MediaMetadataCompat mPreparedMedia;
    private MediaPlayer mediaPlayer;


    public MediaSessionCallback(Context context, MediaSessionCompat mediaSession, MediaPlaybackService service) {
        this.mContext = context;
        this.service = service;
        this.mediaSession = mediaSession;
        this.myMediaPlayerAdapter = new MyMediaPlayerAdapter(context, mediaSession);
        this.myMediaPlayerAdapter.init();
    }

    @Override
    public void onPlay() {
        startService();
        myMediaPlayerAdapter.play();
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle bundle)
    {
        super.onPrepareFromUri(uri, bundle);
        myMediaPlayerAdapter.prepareFromUri(uri);
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle bundle) {
        super.onPlayFromUri(uri, bundle);
        myMediaPlayerAdapter.playFromuri(uri);
    }

    @Override
    public void onStop() {
        service.stopSelf();
        myMediaPlayerAdapter.stop();
        service.stopForeground(false);
    }

    @Override
    public void onPause() {
        myMediaPlayerAdapter.pause();
        // unregister BECOME_NOISY BroadcastReceiver
//        unregisterReceiver(myNoisyAudioStreamReceiver, intentFilter);
        // Take the service out of the foreground, retain the notification
        service.stopForeground(false);
    }

    @Override
    public void onPrepare() {
        if (!mediaSession.isActive()) {
            mediaSession.setActive(true);
        } // if session active
    } // onPrepare

    @Override
    public void onSeekTo(long position )
    {
        myMediaPlayerAdapter.seekTo(position);
    }

    private void startService()
    {
        Intent startServiceIntent = new Intent(mContext, MediaPlaybackService.class);
        startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
        service.startService(startServiceIntent);
    }


    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }


    public MediaNotificationManager getmMediaNotificationManager() {
        return mMediaNotificationManager;
    }

    public MediaPlaybackService getService() {
        return service;
    }
}
