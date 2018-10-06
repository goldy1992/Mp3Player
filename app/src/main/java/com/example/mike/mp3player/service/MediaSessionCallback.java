package com.example.mike.mp3player.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.unusedClasses.MediaNotificationManager;
import com.example.mike.mp3player.unusedClasses.MediaPlayerAdapter;
import com.example.mike.mp3player.unusedClasses.MediaPlayerListener;

import java.io.IOException;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaSessionCallback extends MediaSessionCompat.Callback {
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private MediaSessionCompat mediaSession;
    private Context mContext;
    private MediaPlaybackService service;
    private MediaPlayerAdapter mediaPlayerAdapter;
    private MediaPlayerListener mediaPlayerListener;
    private MediaNotificationManager mMediaNotificationManager;
    private MediaMetadataCompat mPreparedMedia;
    private MediaPlayer mediaPlayer;
    private PlaybackStateCompat.Builder stateBuilder;

    public MediaSessionCallback(Context context, MediaSessionCompat mediaSession, MediaPlaybackService service) {
        this.mContext = context;
        this.service = service;
        this.mediaSession = mediaSession;
        this.mediaPlayerListener = new MediaPlayerListener(mediaSession);
        this.mediaPlayerAdapter = new MediaPlayerAdapter(mContext, mediaPlayerListener);
        this.mediaPlayerListener.getmServiceManager().initServiceManager(this);
        this.mediaPlayer = service.getMediaPlayer();
        this.afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int i) {
            }
        };
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
            startService();
            // Set the session active  (and update metadata and state)
            mediaSession.setActive(true);
            // start the player (custom call)
            mediaPlayer.start();
            stateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 0L, 0f);
            getMediaSession().setPlaybackState(stateBuilder.build());
//            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
//            // Put the service in the foreground, post notification
//            service.startForeground(myPlayerNotification);

        }
    }


    @Override
    public void onPlayFromUri(Uri uri, Bundle bundle) {
        super.onPlayFromUri(uri, bundle);
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for playback, this registers the afChangeListener
        int result = am.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start the service
            startService();
            // Set the session active  (and update metadata and state)
            mediaSession.setActive(true);
            // start the player (custom call)
            try {
                mediaPlayer.setDataSource(mContext, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                stateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 0L, mediaPlayer.getPlaybackParams().getSpeed());
                MediaMetadataCompat.Builder mediaMetadataCompatBuilder = new MediaMetadataCompat.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
                getMediaSession().setMetadata(mediaMetadataCompatBuilder.build());
                getMediaSession().setPlaybackState(stateBuilder.build());

            }
            catch (IOException ex)
            {
                System.err.println(ex);
            }


        }
    }

    @Override
    public void onStop() {
//        unregisterReceiver(myNoisyAudioStreamReceiver);
        // Start the service
        mediaPlayer.stop();
        mediaPlayer.reset();
        stateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_STOPPED, 0L, 0f);
        getMediaSession().setPlaybackState(stateBuilder.build());
        service.stopSelf();
        // Set the session inactive  (and update metadata and state)
        getMediaSession().setActive(false);
        // stop the player (custom call)
//        player.stop();
        // Take the service out of the foreground
        service.stopForeground(false);
    }

    @Override
    public void onPause() {
        // Update metadata and state
        mediaPlayer.pause();
        stateBuilder = new PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, mediaPlayer.getCurrentPosition(), mediaPlayer.getPlaybackParams().getSpeed());
        getMediaSession().setPlaybackState(stateBuilder.build());

        // pause the player (custom call)
//        player.pause();
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

    private void startService()
    {
        Intent startServiceIntent = new Intent(mContext, MediaPlaybackService.class);
        startServiceIntent.setAction("com.example.mike.mp3player.service.MediaPlaybackService");
        service.startService(startServiceIntent);
    }

    private void playMedia(Uri uri) {
            // Start the service
            startService();

            if (null != uri) {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(mContext, uri);

                MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
                MediaMetadataCompat uriMetdata = builder.putText(MediaMetadataCompat.METADATA_KEY_ARTIST, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
                        .build();

                mPreparedMedia = uriMetdata;
                onPrepare();
                mediaPlayerAdapter.playFromMedia(mPreparedMedia);
                mediaPlayerAdapter.playFromUri(uri);

            // start the player (custom call)
 //           mediaPlayerAdapter.onPlay();
            getMediaSession().setActive(true);
//            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
        }

    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    public MediaPlayerAdapter getMediaPlayerAdapter() {
        return mediaPlayerAdapter;
    }

    public MediaPlayerListener getMediaPlayerListener() {
        return mediaPlayerListener;
    }

    public MediaNotificationManager getmMediaNotificationManager() {
        return mMediaNotificationManager;
    }

    public MediaPlaybackService getService() {
        return service;
    }
}
