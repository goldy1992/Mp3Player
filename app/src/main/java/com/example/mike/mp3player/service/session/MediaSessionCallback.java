package com.example.mike.mp3player.service.session;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.player.MarshmallowMediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.NougatMediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.OreoPlayerAdapterBase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.NO_ACTION;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;
import static com.example.mike.mp3player.commons.Constants.PARENT_OBJECT;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static com.example.mike.mp3player.commons.LoggingUtils.logRepeatMode;
import static com.example.mike.mp3player.commons.LoggingUtils.logShuffleMode;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaSessionCallback extends MediaSessionCompat.Callback implements MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    private ServiceManager serviceManager;
    private final PlaybackManager playbackManager;
    private final MediaPlayerAdapterBase mediaPlayerAdapter;
    private final MediaLibrary mediaLibrary;
    private final MediaSessionAdapter mediaSessionAdapter;
    private final AudioBecomingNoisyBroadcastReceiver broadcastReceiver;
    private final Handler worker;
    private static final String LOG_TAG = "MEDIA_SESSION_CALLBACK";
    public static final float DEFAULT_PLAYBACK_SPEED_CHANGE = 0.05f;
    private boolean isInitialised = false;

    /**
     * new constructor to be used for testing and also for future use with dagger2 via the @Inject
     * annotation
     * @param mediaLibrary media library
     * @param playbackManager playback manager
     * @param mediaPlayerAdapterBase media player adapter
     * @param mediaSessionAdapter media session adapter
     * @param serviceManager serviceManager
     * @param broadcastReceiver broadcast receiver
     * @param handler handler
     */
    @Inject
    public MediaSessionCallback(MediaLibrary mediaLibrary,
                                PlaybackManager playbackManager,
                                MediaPlayerAdapterBase mediaPlayerAdapterBase,
                                MediaSessionAdapter mediaSessionAdapter,
                                ServiceManager serviceManager,
                                AudioBecomingNoisyBroadcastReceiver broadcastReceiver,
                                Handler handler) {
        this.mediaLibrary = mediaLibrary;
        this.playbackManager = playbackManager;
        this.mediaPlayerAdapter = mediaPlayerAdapterBase;
        this.mediaSessionAdapter = mediaSessionAdapter;
        this.serviceManager = serviceManager;
        this.broadcastReceiver = broadcastReceiver;
        this.worker = handler;
    }

    public void init() {
        List<MediaBrowserCompat.MediaItem> songList = new ArrayList<>(this.getMediaLibrary().getSongList());
        List<MediaSessionCompat.QueueItem> queueItems = MediaLibraryUtils.convertMediaItemsToQueueItem(songList);
        this.mediaPlayerAdapter.setOnCompletionListener(this::onCompletion);
        this.mediaPlayerAdapter.setOnSeekCompleteListener(this::onSeekComplete);
        this.playbackManager.createNewPlaylist(queueItems);
        Uri firstSongUri = this.getMediaLibrary().getMediaUriFromMediaId(getPlaybackManager().getCurrentMediaId());
        Uri nextSongUri = this.getMediaLibrary().getMediaUriFromMediaId(getPlaybackManager().getNext());
        this.mediaPlayerAdapter.reset(firstSongUri, nextSongUri);
        getMediaSessionAdapter().updateAll();
    }

    @Override
    public synchronized void onPlay() {
        //Log.i(LOG_TAG, "on play, status of worker " + worker.getLooper().getQueue());
        worker.post(() -> this.play());
    }

    private void play() {
        //Log.i(LOG_TAG, "onPlay registed audio noisy receiver");
        if (getMediaPlayerAdapter().play()) {
            getBroadcastReceiver().registerAudioNoisyReceiver();
            //Log.i(LOG_TAG, "onPlay playback started");
            getMediaSessionAdapter().updateAll();
            //Log.i(LOG_TAG, "onPlay mediasession updated");
            getServiceManager().startService();

        }
    }

    @Override
    public synchronized void onSkipToNext() {
        worker.post(() -> this.skipToNext());
    }

    private void skipToNext() {
        String newMediaId = getPlaybackManager().skipToNext();
        if (newMediaId != null) {
            skipToNewMedia(newMediaId);
            getServiceManager().notifyService();
            getMediaSessionAdapter().updateAll();
        }
    }

    @Override
    public synchronized void onSkipToPrevious() {
        worker.post(() -> this.skipToPrevious());
    }

    private void skipToPrevious() {
        //Log.i(LOG_TAG, "skipToPrevious");
        int position = getMediaPlayerAdapter().getCurrentPlaybackPosition();
        if (position > ONE_SECOND) {
            getMediaPlayerAdapter().seekTo(1);
        } else {
            getPlaybackManager().skipToPrevious();
            skipToNewMedia(getPlaybackManager().getCurrentMediaId());
            getServiceManager().notifyService();
            getMediaSessionAdapter().updateAll();
        }

    }
    @Override
    public synchronized boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        //(LOG_TAG, "media button event");
        if (mediaButtonEvent != null && mediaButtonEvent.getExtras() != null
                && mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT) != null) {
            KeyEvent keyEvent = mediaButtonEvent.getExtras().getParcelable(Intent.EXTRA_KEY_EVENT);
            int keyEventCode = keyEvent.getKeyCode();

            switch (keyEventCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY: onPlay(); break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE: onPause(); break;
                case KeyEvent.KEYCODE_MEDIA_NEXT: onSkipToNext(); break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS: onSkipToPrevious(); break;
                default: return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public synchronized void onPrepareFromMediaId(String mediaId, Bundle bundle) {
        worker.post(() -> this.prepareFromMediaId(mediaId, bundle));
    }

    private void prepareFromMediaId(String mediaId, Bundle bundle) {
        //Log.i(LOG_TAG, "prepareFromMediaId");
        super.onPrepareFromMediaId(mediaId, bundle);
        LibraryObject parent = (LibraryObject) bundle.get(PARENT_OBJECT);
        List<MediaBrowserCompat.MediaItem> results = getMediaLibrary().getPlaylist(parent);
        getPlaybackManager().createNewPlaylist(MediaLibraryUtils.convertMediaItemsToQueueItem(results));

        if (mediaId == null) {
            Log.e(LOG_TAG, "received null mediaId");
            return;
        }

        Uri uriToPlay = null;
        Uri followingUri = null;
        for (MediaBrowserCompat.MediaItem m : results) {
            String id = MediaItemUtils.getMediaId(m);
            if (id != null && id.equals(mediaId)) {
                uriToPlay = getMediaLibrary().getMediaUriFromMediaId(id);
                getPlaybackManager().setCurrentItem(mediaId);
                followingUri = getMediaLibrary().getMediaUriFromMediaId(getPlaybackManager().getNext());
                getMediaPlayerAdapter().reset(uriToPlay, followingUri);

                break;
            }
        }
        if (uriToPlay == null) {
            Log.e(LOG_TAG, "failed to find requested uri in collection");
            return;
        }
        getMediaSessionAdapter().updateAll();
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
        getBroadcastReceiver().unregisterAudioNoisyReceiver();
        getMediaPlayerAdapter().pause();
        getMediaSessionAdapter().updateAll();
        getServiceManager().pauseService();
        //Log.i(LOG_TAG, "onPause finished");
    }

    @Override
    public void onSeekTo(long position ) {
        worker.post(() -> this.seekTo(position));

    }

    private void seekTo(long position) {
        //Log.i(LOG_TAG, "seekTO");
        getMediaPlayerAdapter().seekTo(position);
    }

    @Override
    public synchronized void onAddQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> this.addQueueItem(description));
    }

    private void addQueueItem(MediaDescriptionCompat description) {
        //Log.i(LOG_TAG, "addQueueItem");
        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
        getMediaSessionAdapter().setQueue(item);
    }

    @Override
    public synchronized void onRemoveQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> this.removeQueueItem(description));
    }
    private void removeQueueItem(MediaDescriptionCompat description) {
        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
        getMediaSessionAdapter().setQueue(item);
    }

    @Override
    public void onSetRepeatMode (int repeatMode) {
        worker.post(() -> this.setRepeatMode(repeatMode));
    }

    private void setRepeatMode(int repeatMode) {
        //Log.i(LOG_TAG, "set Repeat mode");
        logRepeatMode(repeatMode, LOG_TAG);
        getMediaPlayerAdapter().updateRepeatMode(repeatMode);
        getPlaybackManager().setRepeating(repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL);

        /**
         * TODO: set logic to put in the respective repeat mode
         */
        Bundle bundle = new Bundle();
        bundle.putInt(REPEAT_MODE, repeatMode);
        getMediaSessionAdapter().updateAll(ACTION_SET_REPEAT_MODE);

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
        String nextSongId = getPlaybackManager().getNext();
        Uri nextUri = getMediaLibrary().getMediaUriFromMediaId(nextSongId);
        getMediaPlayerAdapter().setNextMediaPlayer(nextUri);
        getMediaSessionAdapter().updateAll(ACTION_SET_SHUFFLE_MODE);
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
        if (mediaPlayer != null && mediaPlayer.equals(getMediaPlayerAdapter().getCurrentMediaPlayer())) {
            Log.i(LOG_TAG, "looping " + mediaPlayer.isLooping());
            if (mediaPlayer.isLooping()) {
            } else {
                getPlaybackManager().notifyPlaybackComplete(); // increments queue
                String nextUriToPrepare = getPlaybackManager().getNext(); // gets uri after newly incremented index
                if (null != nextUriToPrepare) {
                    Uri nextItemUri = getMediaLibrary().getMediaUriFromMediaId(nextUriToPrepare);
                    getMediaPlayerAdapter().onComplete(nextItemUri);
                    getMediaSessionAdapter().updateAll();
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
            case INCREASE_PLAYBACK_SPEED: getMediaPlayerAdapter().increaseSpeed(DEFAULT_PLAYBACK_SPEED_CHANGE);
                break;
            case DECREASE_PLAYBACK_SPEED: getMediaPlayerAdapter().decreaseSpeed(DEFAULT_PLAYBACK_SPEED_CHANGE);
                break;
            default: break;
        }
        getMediaSessionAdapter().updateAll();
    }

    private void skipToNewMedia(String newMediaId) {
        Uri newUri = getMediaLibrary().getMediaUriFromMediaId(newMediaId);
        Uri followingUri = getMediaLibrary().getMediaUriFromMediaId(getPlaybackManager().getNext());
        PlaybackStateCompat currentState = getMediaSessionAdapter().getCurrentPlaybackState(ACTION_SEEK_TO);
        getMediaPlayerAdapter().reset(newUri, followingUri);
        if (currentState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            getMediaPlayerAdapter().play();
        }
    }

    /**
     * @param serviceManager service manager
     */
    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        getMediaSessionAdapter().updateAll(NO_ACTION);
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
    public MediaPlayerAdapterBase getMediaPlayerAdapter() {
        return mediaPlayerAdapter;
    }
    @VisibleForTesting
    public MediaLibrary getMediaLibrary() {
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
}