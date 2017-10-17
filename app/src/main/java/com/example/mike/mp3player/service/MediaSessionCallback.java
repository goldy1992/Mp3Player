package com.example.mike.mp3player.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaSessionCallback extends MediaSessionCompat.Callback {
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private MediaSessionCompat mediaSession;
    private MediaBrowserServiceCompat service;
    private Context mContext;
    private MyMediaPlayer player;

    public MediaSessionCallback(Context context) {
        this.mContext = context;
    }


    @Override
    public void onPlay() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for playback, this registers the afChangeListener
        int result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start the service
            Intent intent = new Intent(mContext, MediaPlaybackService.class);
             service.startService(intent);
            // Set the session active  (and update metadata and state)
            mediaSession.setActive(true);
            // start the player (custom call)
            player.start();
            // Register BECOME_NOISY BroadcastReceiver
            //registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
            // Put the service in the foreground, post notification
            //service.startForeground(myPlayerNotification);
        }
    }

    @Override
    public void onStop() {
        AudioManager am = (AudioManager)  mContext.getSystemService(Context.AUDIO_SERVICE);
        // Abandon audio focus
        am.abandonAudioFocus(afChangeListener);
        //unregisterReceiver(myNoisyAudioStreamReceiver);
        // Start the service
        service.stopSelf();
        // Set the session inactive  (and update metadata and state)
        mediaSession.setActive(false);
        // stop the player (custom call)
        player.stop();
        // Take the service out of the foreground
        service.stopForeground(false);
    }

    @Override
    public void onPause() {
        AudioManager am = (AudioManager)  mContext.getSystemService(Context.AUDIO_SERVICE);
        // Update metadata and state
        // pause the player (custom call)
        player.pause();
        // unregister BECOME_NOISY BroadcastReceiver
        //unregisterReceiver(myNoisyAudioStreamReceiver, intentFilter);
        // Take the service out of the foreground, retain the notification
        service.stopForeground(false);
    }
}
