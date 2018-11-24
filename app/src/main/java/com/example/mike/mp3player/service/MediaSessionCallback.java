package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.commons.Constants.PLAYLIST;
import static com.example.mike.mp3player.commons.Constants.PLAY_ALL;

/**
 * Created by Mike on 24/09/2017.
 */

public class MediaSessionCallback extends MediaSessionCompat.Callback implements MediaPlayer.OnCompletionListener {

    private final List<MediaSessionCompat.QueueItem> playlist = new ArrayList<>();
    private int repeatMode = PlaybackStateCompat.REPEAT_MODE_ALL;
    private int queueIndex = -1;
    private ServiceManager serviceManager;
    private MyMediaPlayerAdapter myMediaPlayerAdapter;
    private MediaSessionCompat mediaSession;
    private MyNotificationManager myNotificationManager;
    private MediaLibrary mediaLibrary;
    private static final String LOG_TAG = "MEDIA_SESSION_CALLBACK";

    public MediaSessionCallback(Context context, MyNotificationManager myNotificationManager, ServiceManager serviceManager, MediaSessionCompat mediaSession, MediaLibrary mediaLibrary) {
        this.serviceManager = serviceManager;
        this.mediaSession = mediaSession;
        this.mediaLibrary = mediaLibrary;
        this.myNotificationManager = myNotificationManager;
        this.myMediaPlayerAdapter = new MyMediaPlayerAdapter(context);
        this.myMediaPlayerAdapter.init();
    }

    @Override
    public void onPlay() {
        myMediaPlayerAdapter.play();
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState());
        mediaSession.setMetadata(myMediaPlayerAdapter.getCurrentMetaData());

        serviceManager.startService(prepareNotification());
    }

    @Override
    public void onSkipToNext() {
        int currentState = myMediaPlayerAdapter.getCurrentState();
        int newQueueIndex = queueIndex + 1;
        if (newQueueIndex >= playlist.size() || newQueueIndex < 0) {
            return;
        } else {
            queueIndex = newQueueIndex;
            String newMediaId = playlist.get(queueIndex).getDescription().getMediaId();
            Uri newUri = mediaLibrary.getMediaUri(newMediaId);
            myMediaPlayerAdapter.prepareFromUri(newUri);

            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                myMediaPlayerAdapter.play();
            }
        }

    }

    @Override
    public void onSkipToPrevious() {

    }
    @Override
    public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        if (mediaButtonEvent != null && mediaButtonEvent.getExtras() != null
                && mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT) != null) {
            KeyEvent keyEvent = mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT);
            int keyEventCode = keyEvent.getKeyCode();

            switch (keyEventCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY: onPlay(); break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE: onPause(); break;
                default: break;
            }
            return true;
        }
        return false;

    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle bundle) {
        super.onPrepareFromUri(uri, bundle);
        myMediaPlayerAdapter.prepareFromUri(uri);
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState());
        mediaSession.setMetadata(myMediaPlayerAdapter.getCurrentMetaData());
    }

    @Override
    public void onPrepareFromMediaId(String mediaId, Bundle bundle) {
        super.onPrepareFromMediaId(mediaId, bundle);

        if (bundle.containsKey(PLAYLIST)) {
              if (bundle.getString(PLAYLIST).equals(PLAY_ALL)) {
                  playlist.clear();;
                  playlist.addAll(MediaLibraryUtils.convertMediaItemsToQueueItem(mediaLibrary.getLibrary()));
              }
              Integer integerQueueIndex = MediaLibraryUtils.findIndexOfTrackInPlaylist(playlist, mediaId);
              if (integerQueueIndex == null) {
                  queueIndex = -1;
              } else {
                  queueIndex = integerQueueIndex;
              }
        }
        Uri uri = mediaLibrary.getMediaUri(mediaId);
        myMediaPlayerAdapter.prepareFromUri(uri);
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState());
        mediaSession.setMetadata(myMediaPlayerAdapter.getCurrentMetaData());
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle bundle) {
        super.onPlayFromUri(uri, bundle);
        myMediaPlayerAdapter.prepareFromUri(uri);
        myMediaPlayerAdapter.play();
        serviceManager.startMediaSession();
    }

    @Override
    public void onStop() {
        myMediaPlayerAdapter.stop();
        serviceManager.stopService();
    }

    @Override
    public void onPause() {
        myMediaPlayerAdapter.pause();
        // unregister BECOME_NOISY BroadcastReceiver
//        unregisterReceiver(myNoisyAudioStreamReceiver, intentFilter);
        // Take the serviceManager out of the foreground, retain the notification
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState());
        serviceManager.pauseService(prepareNotification());
    }

    @Override
    public void onSeekTo(long position ) {
        myMediaPlayerAdapter.seekTo(position);
    }

    @Override
    public void onAddQueueItem(MediaDescriptionCompat description) {
        playlist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
        queueIndex = (queueIndex == -1) ? 0 : queueIndex;
        mediaSession.setQueue(playlist);
    }

    @Override
    public void onRemoveQueueItem(MediaDescriptionCompat description) {
        playlist.remove(new MediaSessionCompat.QueueItem(description, description.hashCode()));
        queueIndex = (playlist.isEmpty()) ? -1 : queueIndex;
        mediaSession.setQueue(playlist);
    }

    private Notification prepareNotification() {
        return myNotificationManager.getNotification(myMediaPlayerAdapter.getCurrentMetaData(),
                myMediaPlayerAdapter.getMediaPlayerState(),
                mediaSession.getSessionToken());
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        queueIndex++;
        if (!playlist.isEmpty() && queueIndex < playlist.size()) {
            playlist.get(queueIndex);
        }
    }
}