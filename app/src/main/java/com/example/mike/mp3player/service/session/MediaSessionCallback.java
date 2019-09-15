package com.example.mike.mp3player.service.session;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.player.ExoPlayerAdapter;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
import static com.example.mike.mp3player.commons.Constants.ACTION_PLAYBACK_SPEED_CHANGED;
import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.ID_DELIMITER;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.NO_ACTION;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static com.example.mike.mp3player.commons.LoggingUtils.logRepeatMode;
import static com.example.mike.mp3player.commons.LoggingUtils.logShuffleMode;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaSessionCallback extends MediaSessionCompat.Callback implements MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    private final ServiceManager serviceManager;
    private final PlaybackManager playbackManager;
    private final ExoPlayerAdapter mediaPlayerAdapter;
    private final ContentManager mediaLibrary;
    private final MediaSessionAdapter mediaSessionAdapter;
    private final AudioBecomingNoisyBroadcastReceiver broadcastReceiver;
    private final Handler worker;
    private static final String LOG_TAG = "MEDIA_SESSION_CALLBACK";
    public static final float DEFAULT_PLAYBACK_SPEED_CHANGE = 0.05f;

    /**
     * new constructor to be used for testing and also for future use with dagger2 via the @Inject
     * annotation
     * @param mediaLibrary media library
     * @param playbackManager playback manager
     * @param mediaPlayerAdapter media player adapter
     * @param mediaSessionAdapter media session adapter
     * @param serviceManager serviceManager
     * @param broadcastReceiver broadcast receiver
     * @param handler handler
     */
    @Inject
    public MediaSessionCallback(ContentManager mediaLibrary,
                                PlaybackManager playbackManager,
                                ExoPlayerAdapter mediaPlayerAdapter,
                                MediaSessionAdapter mediaSessionAdapter,
                                ServiceManager serviceManager,
                                AudioBecomingNoisyBroadcastReceiver broadcastReceiver,
                                Handler handler) {
        this.mediaLibrary = mediaLibrary;
        this.playbackManager = playbackManager;
        this.mediaPlayerAdapter = mediaPlayerAdapter;
        this.mediaSessionAdapter = mediaSessionAdapter;
        this.serviceManager = serviceManager;
        this.broadcastReceiver = broadcastReceiver;
        this.worker = handler;
    }

    public void init() {
        this.mediaPlayerAdapter.setOnCompletionListener(this::onCompletion);
        this.mediaPlayerAdapter.setOnSeekCompleteListener(this::onSeekComplete);
        this.broadcastReceiver.setMediaSessionCallback(this);
        Uri firstSongUri = playbackManager.getCurrentMediaUri();
        Uri nextSongUri = playbackManager.getNext();
        this.e
        this.mediaPlayerAdapter.reset(firstSongUri, nextSongUri);
        mediaSessionAdapter.updateAll(NO_ACTION);
    }

    @Override
    public synchronized void onPlay() {
        //Log.i(LOG_TAG, "on play, status of worker " + worker.getLooper().getQueue());
        worker.post(() -> this.play());
    }

    private void play() {
        //Log.i(LOG_TAG, "onPlay registed audio noisy receiver");
        if (mediaPlayerAdapter.play()) {
            broadcastReceiver.registerAudioNoisyReceiver();
            //Log.i(LOG_TAG, "onPlay playback started");
            mediaSessionAdapter.updateAll(ACTION_PLAY
            );
            //Log.i(LOG_TAG, "onPlay mediasession updated");
            serviceManager.startService();
        }
    }

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
        worker.post(() -> this.playFromMediaId(mediaId, extras));
    }

    private void playFromMediaId(String mediaId, Bundle extras) {
        prepareFromMediaId(mediaId, extras);
        play();
    }

    @Override
    public synchronized void onSkipToNext() {
        worker.post(() -> this.skipToNext());
    }

    private void skipToNext() {
        Uri newMediaId = getPlaybackManager().skipToNext();
        if (newMediaId != null) {
            skipToNewMedia(newMediaId);
            getServiceManager().notifyService();
            mediaSessionAdapter.updateAll(ACTION_SKIP_TO_NEXT);
        }
    }

    @Override
    public synchronized void onSkipToPrevious() {
        worker.post(() -> this.skipToPrevious());
    }

    private void skipToPrevious() {
        //Log.i(LOG_TAG, "skipToPrevious");
        int position = mediaPlayerAdapter.getCurrentPlaybackPosition();
        if (position > ONE_SECOND) {
            mediaPlayerAdapter.seekTo(1);
        } else {
            getPlaybackManager().skipToPrevious();
            skipToNewMedia(getPlaybackManager().getCurrentMediaUri());
            getServiceManager().notifyService();
            mediaSessionAdapter.updateAll(ACTION_SKIP_TO_PREVIOUS);
        }

    }
    /** {@inheritDoc} */
    @Override
    public synchronized boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        if (mediaButtonEvent == null) {
            return false;
        }

        //(LOG_TAG, "media button event");
        if (mediaButtonEvent.getExtras() != null
                && mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT) != null) {
            KeyEvent keyEvent = mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT);
            int keyEventCode = keyEvent.getKeyCode();

            switch (keyEventCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY: onPlay(); return true;
                case KeyEvent.KEYCODE_MEDIA_PAUSE: onPause(); return true;
                case KeyEvent.KEYCODE_MEDIA_NEXT: onSkipToNext(); return true;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS: onSkipToPrevious(); return true;
            }
        }
        return super.onMediaButtonEvent(mediaButtonEvent);
    }

    @Override
    public synchronized void onPrepareFromMediaId(String mediaId, Bundle bundle) {
        worker.post(() -> this.prepareFromMediaId(mediaId, bundle));
    }

    private void prepareFromMediaId(String mediaId, Bundle bundle) {
        //Log.i(LOG_TAG, "prepareFromMediaId");
        super.onPrepareFromMediaId(mediaId, bundle);
        String trackId = extractTrackId(mediaId);
        if (null != trackId) {
            List<MediaBrowserCompat.MediaItem> results = mediaLibrary.getPlaylist(mediaId);
            if (null != results) {
                playbackManager.createNewPlaylist(results);

                Uri uriToPlay = null;
                Uri followingUri = null;
                for (MediaBrowserCompat.MediaItem m : results) {
                    String id = MediaItemUtils.getMediaId(m);
                    if (id != null && id.equals(trackId)) {
                        uriToPlay = m.getDescription().getMediaUri();
                        playbackManager.setCurrentItem(trackId);
                        followingUri = getPlaybackManager().getNext();
                        mediaPlayerAdapter.reset(uriToPlay, followingUri);

                        break;
                    }
                }
                if (uriToPlay == null) {
                    Log.e(LOG_TAG, "failed to find requested uri in collection");
                    return;
                }
                mediaSessionAdapter.updateAll(ACTION_PREPARE_FROM_MEDIA_ID);
            }
        }
    }

    @Override
    public synchronized void onStop() {
        super.onStop();
    }

    @Override
    public synchronized void onPause() {
        worker.post(() -> this.pause());
    }
    private void pause() {
        //Log.i(LOG_TAG, "onPause");
        if (mediaPlayerAdapter.pause()) {
            broadcastReceiver.unregisterAudioNoisyReceiver();
            mediaSessionAdapter.updateAll(ACTION_PAUSE);
            serviceManager.pauseService();
        }
        //Log.i(LOG_TAG, "onPause finished");
    }

    @Override
    public void onSeekTo(long position ) {
        worker.post(() -> this.seekTo(position));

    }

    private void seekTo(long position) {
        //Log.i(LOG_TAG, "seekTO");
        mediaPlayerAdapter.seekTo(position);
    }

    @Override
    public synchronized void onAddQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> this.addQueueItem(description));
    }

    private void addQueueItem(MediaDescriptionCompat description) {
//        //Log.i(LOG_TAG, "addQueueItem");
//        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
//        mediaSessionAdapter.setQueue(item);
    }

    @Override
    public synchronized void onRemoveQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> this.removeQueueItem(description));
    }
    private void removeQueueItem(MediaDescriptionCompat description) {
//        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
//        mediaSessionAdapter.setQueue(item);
    }

    @Override
    public void onSetRepeatMode (int repeatMode) {
        worker.post(() -> this.setRepeatMode(repeatMode));
    }

    private void setRepeatMode(int repeatMode) {
        //Log.i(LOG_TAG, "set Repeat mode");
        logRepeatMode(repeatMode, LOG_TAG);
        mediaPlayerAdapter.updateRepeatMode(repeatMode);
        getPlaybackManager().setRepeating(repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL);

        /**
         * TODO: set logic to put in the respective repeat mode
         */
        Bundle bundle = new Bundle();
        bundle.putInt(REPEAT_MODE, repeatMode);
        mediaSessionAdapter.updateAll(ACTION_SET_REPEAT_MODE);

    }

    @Override
    public void onSetShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        worker.post(() -> this.setShuffleMode(shuffleMode));
    }

    private void setShuffleMode(@PlaybackStateCompat.ShuffleMode int shuffleMode) {
        logShuffleMode(shuffleMode, LOG_TAG);
        // TODO: set shuffle mode and therefore set the next mediaplayers accordingly
        boolean shuffleOn = shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL;
        getPlaybackManager().setShuffle(shuffleOn);
        Uri nextSongUri = getPlaybackManager().getNext();
        mediaPlayerAdapter.setNextMediaPlayer(nextSongUri);
        mediaSessionAdapter.updateAll(ACTION_SET_SHUFFLE_MODE);
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
        Log.i(LOG_TAG, "on playback complete hit");
        if (mediaPlayer != null && mediaPlayer.equals(mediaPlayerAdapter.getCurrentMediaPlayer())) {
            Log.i(LOG_TAG, "looping " + mediaPlayer.isLooping());
            if (mediaPlayer.isLooping()) {
            } else {
                getPlaybackManager().notifyPlaybackComplete(); // increments queue
                Uri nextUriToPrepare = getPlaybackManager().getNext(); // gets uri after newly incremented index
                if (null != nextUriToPrepare) {
                    mediaPlayerAdapter.onComplete(nextUriToPrepare);
                    mediaSessionAdapter.updateAll(ACTION_PLAY_FROM_MEDIA_ID);
                }
            }
            getServiceManager().notifyService();
        }
    }

    @Override
    public synchronized void onCustomAction(String customAction, Bundle extras) {
        worker.post(() -> this.customAction(customAction, extras));
    }

    private void customAction(String customAction, Bundle extras) {
        Log.i(LOG_TAG, "hit speed change");
        super.onCustomAction(customAction, extras);
        switch (customAction) {
            case INCREASE_PLAYBACK_SPEED: mediaPlayerAdapter.increaseSpeed(DEFAULT_PLAYBACK_SPEED_CHANGE);
                break;
            case DECREASE_PLAYBACK_SPEED: mediaPlayerAdapter.decreaseSpeed(DEFAULT_PLAYBACK_SPEED_CHANGE);
                break;
            default: break;
        }
        mediaSessionAdapter.updateAll(ACTION_PLAYBACK_SPEED_CHANGED);
    }

    private void skipToNewMedia(Uri newMediaUri) {
        Uri newUri = newMediaUri;
        Uri followingUri = getPlaybackManager().getNext();
        PlaybackStateCompat currentState = mediaSessionAdapter.getCurrentPlaybackState(ACTION_PLAY_FROM_MEDIA_ID);
        mediaPlayerAdapter.reset(newUri, followingUri);
        if (currentState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            mediaPlayerAdapter.play();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mediaSessionAdapter.updateAll(ACTION_SEEK_TO);
    }

    @VisibleForTesting
    public ServiceManager getServiceManager() {
        return serviceManager;
    }
    @VisibleForTesting
    public PlaybackManager getPlaybackManager() {
        return playbackManager;
    }
    @VisibleForTesting
    public MediaPlayerAdapter getMediaPlayerAdapter() {
        return mediaPlayerAdapter;
    }
    @VisibleForTesting
    public ContentManager getMediaLibrary() {
        return mediaLibrary;
    }
    @VisibleForTesting
    public MediaSessionAdapter getMediaSessionAdapter() {
        return mediaSessionAdapter;
    }

    @VisibleForTesting
    public AudioBecomingNoisyBroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    private String extractTrackId(String mediaId) {
        if (null != mediaId) {
            List<String> splitId = Arrays.asList(mediaId.split(ID_DELIMITER));
            if (!splitId.isEmpty()) {
                return splitId.get(splitId.size() - 1);
            }
        }
        else {
            Log.e(LOG_TAG, "received null mediaId");
        }
        return null;
    }
}