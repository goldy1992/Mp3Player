package com.example.mike.mp3player.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.LibraryId;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.library.utils.ValidMetaDataUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaSessionCallback extends MediaSessionCompat.Callback implements MediaPlayer.OnCompletionListener {

    private static final int NO_ACTION = 0;
    private ServiceManager serviceManager;
    private PlaybackManager playbackManager;
    private MyMediaPlayerAdapter myMediaPlayerAdapter;
    private MediaSessionCompat mediaSession;
    private MyNotificationManager myNotificationManager;
    private MediaLibrary mediaLibrary;
    private ReceiveBroadcasts broadcastReceiver;
    private Context context;
    private Handler worker;
    private static final String LOG_TAG = "MEDIA_SESSION_CALLBACK";

    public MediaSessionCallback(Context context, MyNotificationManager myNotificationManager,
                                ServiceManager serviceManager, MediaSessionCompat mediaSession,
                                MediaLibrary mediaLibrary, HandlerThread worker) {
        this.serviceManager = serviceManager;
        this.mediaSession = mediaSession;
        this.mediaLibrary = mediaLibrary;
        this.myNotificationManager = myNotificationManager;
        this.playbackManager = new PlaybackManager();
        this.myMediaPlayerAdapter = new MyMediaPlayerAdapter(context);
        this.broadcastReceiver = new ReceiveBroadcasts();
        this.context = context;
        this.worker = new Handler(worker.getLooper());
    }

    public void init() {
        List<MediaSessionCompat.QueueItem> queueItems = MediaLibraryUtils.convertMediaItemsToQueueItem(new ArrayList<>(this.mediaLibrary.getSongList()));
        this.playbackManager.init(queueItems);
        Uri firstSongUri = this.mediaLibrary.getMediaUriFromMediaId(playbackManager.getCurrentMediaId());
        Uri nextSongUri = this.mediaLibrary.getMediaUriFromMediaId(playbackManager.getNext());
        this.myMediaPlayerAdapter.reset(firstSongUri, nextSongUri, this);
        this.myMediaPlayerAdapter.getCurrentMediaPlayer().setOnCompletionListener(this);
        updateMediaSession(ACTION_PREPARE_FROM_MEDIA_ID);
    }

    @Override
    public synchronized void onPlay() {
        worker.post(this::play);
    }

    private void play() {
        Log.i(LOG_TAG, "onPlay");
        broadcastReceiver.registerAudioNoisyReceiver();
        Log.i(LOG_TAG, "onPlay registed audio noisy receiver");
        myMediaPlayerAdapter.play();
        Log.i(LOG_TAG, "onPlay playback started");
        updateMediaSession(ACTION_PLAY);
        Log.i(LOG_TAG, "onPlay mediasession updated");
        serviceManager.startService(prepareNotification(ACTION_PLAY));
    }

        @Override
    public synchronized void onSkipToNext() {
        worker.post(this::skipToNext);
    }

    private void skipToNext() {
        String newMediaId = playbackManager.skipToNext();
        if (newMediaId != null) {
            skipToNewMedia(newMediaId);
            serviceManager.notify(prepareNotification(ACTION_SKIP_TO_NEXT));
        }
    }

    @Override
    public synchronized void onSkipToPrevious() {
        worker.post(this::skipToPrevious);
    }

    private void skipToPrevious() {
        int position = myMediaPlayerAdapter.getCurrentPlaybackPosition();
        if (position > ONE_SECOND) {
            myMediaPlayerAdapter.seekTo(1);
        } else {
            playbackManager.skipToPrevious();
        }
        skipToNewMedia(playbackManager.getCurrentMediaId());
        serviceManager.notify(prepareNotification(ACTION_SKIP_TO_PREVIOUS));
    }
    @Override
    public synchronized boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        Log.i(LOG_TAG, "media button event");
        if (mediaButtonEvent != null && mediaButtonEvent.getExtras() != null
                && mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT) != null) {
            KeyEvent keyEvent = mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT);
            int keyEventCode = keyEvent.getKeyCode();

            switch (keyEventCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY: onPlay(); break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE: onPause(); break;
                case KeyEvent.KEYCODE_MEDIA_NEXT: onSkipToNext(); break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS: onSkipToPrevious(); break;
                default: break;
            }
            return true;
        }
        return false;
    }


    @Override
    public synchronized void onPrepareFromUri(Uri uri, Bundle bundle) {
        worker.post(() -> prepareFromUri(uri, bundle));
    }

    private void prepareFromUri(Uri uri, Bundle bundle) {
//        super.onPrepareFromUri(uri, bundle);
//        myMediaPlayerAdapter.prepareFromUri(uri);
//        updateMediaSession();
    }

    @Override
    public synchronized void onPrepareFromMediaId(String mediaId, Bundle bundle) {
        worker.post(() -> prepareFromMediaId(mediaId, bundle));
    }

    private void prepareFromMediaId(String mediaId, Bundle bundle) {
        super.onPrepareFromMediaId(mediaId, bundle);
        LibraryId parentId = (LibraryId) bundle.get(PARENT_ID);
        List<MediaBrowserCompat.MediaItem> results = mediaLibrary.getPlaylist(parentId);
        playbackManager.createNewPlaylist(MediaLibraryUtils.convertMediaItemsToQueueItem(results));

        if (mediaId == null) {
            Log.e(LOG_TAG, "received null mediaId");
            return;
        }

        Uri uriToPlay = null;
        Uri followingUri = null;
        for (MediaBrowserCompat.MediaItem m : results) {
            String id = MediaItemUtils.getMediaId(m);
            if (id != null && id.equals(mediaId)) {
                uriToPlay = mediaLibrary.getMediaUriFromMediaId(id);
                playbackManager.setQueueIndex(mediaId);
                followingUri = mediaLibrary.getMediaUriFromMediaId(playbackManager.getNext());
                myMediaPlayerAdapter.reset(uriToPlay, followingUri, this);

                break;
            }
        }
        if (uriToPlay == null) {
            Log.e(LOG_TAG, "failed to find requested uri in collection");
            return;
        }

        updateMediaSession(ACTION_PREPARE_FROM_MEDIA_ID);
    }

    @Override
    public synchronized void onPlayFromUri(Uri uri, Bundle bundle) {
//        Log.i(LOG_TAG, "play from uri");
//        super.onPlayFromUri(uri, bundle);
//        myMediaPlayerAdapter.prepareFromUri(uri);
//        myMediaPlayerAdapter.play();
//        broadcastReceiver.registerAudioNoisyReceiver();
//        serviceManager.startMediaSession();
//        updateMediaSession();
    }

    @Override
    public synchronized void onStop() {
        super.onStop();
    }

    @Override
    public synchronized void onPause() {
        worker.post(this::pause);
    }
    private void pause() {
        Log.i(LOG_TAG, "onPause");
        broadcastReceiver.unregisterAudioNoisyReceiver();
        myMediaPlayerAdapter.pause();
        updateMediaSession(ACTION_PAUSE);
        serviceManager.pauseService(prepareNotification(ACTION_PAUSE));
        Log.i(LOG_TAG, "onPause finished");
    }

    @Override
    public void onSeekTo(long position ) {
        worker.post(() -> seekTo(position));

    }
    private void seekTo(long position) {
        myMediaPlayerAdapter.seekTo(position);
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState(ACTION_SEEK_TO));
    }

    @Override
    public synchronized void onAddQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> addQueueItem(description));
    }

    private void addQueueItem(MediaDescriptionCompat description) {
        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
        mediaSession.setQueue(playbackManager.onAddQueueItem(item));
    }

    @Override
    public synchronized void onRemoveQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> removeQueueItem(description));
    }
    private void removeQueueItem(MediaDescriptionCompat description) {
        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
        mediaSession.setQueue(playbackManager.onRemoveQueueItem(item));
    }

    @Override
    public void onSetRepeatMode (int repeatMode) {
        PlaybackStateCompat newState = myMediaPlayerAdapter.updateRepeatMode(repeatMode);
        PlaybackStateCompat currentState = myMediaPlayerAdapter.getMediaPlayerState(ACTION_SET_REPEAT_MODE);

        mediaSession.setPlaybackState(newState);
        /**
         * TODO: set logic to put in the respective repeat mode
         */

        updateMediaSession(ACTION_SET_REPEAT_MODE);
    }

    /**
     * When playback is complete,
     * IF NOT LAST SONG
     *  1) IN PLAYLIST: increment to next song 'nextSong'
     *      IF EXISTS SONG AFTER nextSong
     *          setNextMediaPlayer
     * @param mediaPlayer the mediaplayer which has conpleted playback
     */
    @Override
    public synchronized void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null && mediaPlayer.equals(myMediaPlayerAdapter.getCurrentMediaPlayer())) {
            playbackManager.notifyPlaybackComplete(); // increments queue
            String nextUriToPrepare = playbackManager.getNext();
            if (null != nextUriToPrepare) {
                Uri nextItemUri = mediaLibrary.getMediaUriFromMediaId(nextUriToPrepare);
                myMediaPlayerAdapter.onComplete(nextItemUri, this);
            }
            updateMediaSession(NO_ACTION);
            serviceManager.notify(prepareNotification(NO_ACTION));
        }
    }

    @Override
    public synchronized void onCustomAction(String customAction, Bundle extras) {
        worker.post(() -> customAction(customAction, extras));
    }

    private void customAction(String customAction, Bundle extras) {
        super.onCustomAction(customAction, extras);
        switch (customAction) {
            case INCREASE_PLAYBACK_SPEED: myMediaPlayerAdapter.increaseSpeed(0.05f);
                break;
            case DECREASE_PLAYBACK_SPEED: myMediaPlayerAdapter.decreaseSpeed(0.05f);
                break;
            default: break;
        }
        updateMediaSession(NO_ACTION);
    }

    private Notification prepareNotification(long actions) {
        return myNotificationManager.getNotification(getCurrentMetaData(),
                myMediaPlayerAdapter.getMediaPlayerState(actions),
                mediaSession.getSessionToken());
    }

    private MediaMetadataCompat getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = myMediaPlayerAdapter.getCurrentMetaData();
        MediaSessionCompat.QueueItem currentItem = playbackManager.getCurrentItem();
        if (ValidMetaDataUtil.validTitle(currentItem)) {
            builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentItem.getDescription().getTitle().toString());
        } else {
            builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, UNKNOWN);
        }

        if (ValidMetaDataUtil.validArtist(currentItem)) {
            builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentItem.getDescription().getExtras().getString(STRING_METADATA_KEY_ARTIST));
        } else {
            builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, UNKNOWN);
        }
        return builder.build();
    }

    private void skipToNewMedia(String newMediaId) {
        Uri newUri = mediaLibrary.getMediaUriFromMediaId(newMediaId);
        Uri followingUri = mediaLibrary.getMediaUriFromMediaId(playbackManager.getNext());
        PlaybackStateCompat currentState = myMediaPlayerAdapter.getMediaPlayerState(NO_ACTION);
        myMediaPlayerAdapter.reset(newUri, followingUri, this);
        if (currentState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            myMediaPlayerAdapter.play();
        }
    }

    private void updateMediaSession(long actions) {
        mediaSession.setPlaybackState(myMediaPlayerAdapter.getMediaPlayerState(actions));
        mediaSession.setMetadata(getCurrentMetaData());
    }

    private class ReceiveBroadcasts extends BroadcastReceiver {
        private boolean audioNoisyReceiverRegistered;
        private final IntentFilter AUDIO_NOISY_INTENT_FILTER =
                new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

        @Override
        public synchronized void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (myMediaPlayerAdapter.isPlaying()) {
                    myMediaPlayerAdapter.pause();
                    updateMediaSession(ACTION_PAUSE);
                    serviceManager.notify(prepareNotification(ACTION_PAUSE));
                }
            }
        }

        private void registerAudioNoisyReceiver() {
            if (!audioNoisyReceiverRegistered) {
                context.registerReceiver(this, AUDIO_NOISY_INTENT_FILTER);
                audioNoisyReceiverRegistered = true;
            }
        }

        private void unregisterAudioNoisyReceiver() {
            if (audioNoisyReceiverRegistered) {
                context.unregisterReceiver(this);
                audioNoisyReceiverRegistered = false;
            }
        }
    } // ReceivesBroadcasts class
}