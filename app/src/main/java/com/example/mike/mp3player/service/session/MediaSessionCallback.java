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

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MyNotificationManager;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;
import com.example.mike.mp3player.service.player.MarshmallowMediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.OreoPlayerAdapterBase;
import com.example.mike.mp3player.service.player.NougatMediaPlayerAdapterBase;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.NO_ACTION;
import static com.example.mike.mp3player.commons.Constants.ONE_SECOND;
import static com.example.mike.mp3player.commons.Constants.PARENT_OBJECT;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static com.example.mike.mp3player.commons.Constants.SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.LoggingUtils.logRepeatMode;
import static com.example.mike.mp3player.commons.LoggingUtils.logShuffleMode;

/**
 * Created by Mike on 24/09/2017.
 */
public class MediaSessionCallback extends MediaSessionCompat.Callback implements MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener {
    private ServiceManager serviceManager;
    private PlaybackManager playbackManager;
    private MediaPlayerAdapterBase mediaPlayerAdapter;
    private MediaLibrary mediaLibrary;
    private MediaSessionAdapter mediaSessionAdapter;
    private AudioBecomingNoisyBroadcastReceiver broadcastReceiver;
    private Context context;
    private Handler worker;
    private static final String LOG_TAG = "MEDIA_SESSION_CALLBACK";
    private boolean isInitialised = false;

    public MediaSessionCallback(MediaPlaybackService service, MyNotificationManager myNotificationManager,
                                MediaSessionCompat mediaSession,
                                MediaLibrary mediaLibrary, Looper looper) {
        this.serviceManager = serviceManager;
        this.mediaLibrary = mediaLibrary;
        List<MediaBrowserCompat.MediaItem> songList = new ArrayList<>(this.mediaLibrary.getSongList());
        List<MediaSessionCompat.QueueItem> queueItems = MediaLibraryUtils.convertMediaItemsToQueueItem(songList);
        this.playbackManager = new PlaybackManager(queueItems);
        this.mediaPlayerAdapter = createMediaPlayerAdapter(context);
        this.mediaSessionAdapter = new MediaSessionAdapter(mediaSession, playbackManager, mediaPlayerAdapter);
        this.context = service.getApplicationContext();
        this.serviceManager = new ServiceManager(service, mediaSessionAdapter);
        this.broadcastReceiver = new AudioBecomingNoisyBroadcastReceiver(context, mediaSessionAdapter, mediaPlayerAdapter, serviceManager);

        this.worker = new Handler(looper);
        init();
    }

    private void init() {
        Uri firstSongUri = this.mediaLibrary.getMediaUriFromMediaId(playbackManager.getCurrentMediaId());
        Uri nextSongUri = this.mediaLibrary.getMediaUriFromMediaId(playbackManager.getNext());
        this.mediaPlayerAdapter.reset(firstSongUri, nextSongUri);
        mediaSessionAdapter.updateAll();
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
            mediaSessionAdapter.updateAll();
            //Log.i(LOG_TAG, "onPlay mediasession updated");
            serviceManager.startService();

        }
    }

    @Override
    public synchronized void onSkipToNext() {
        worker.post(() -> this.skipToNext());
    }

    private void skipToNext() {
        String newMediaId = playbackManager.skipToNext();
        if (newMediaId != null) {
            skipToNewMedia(newMediaId);
            serviceManager.notifyService();
            mediaSessionAdapter.updateAll();
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
            playbackManager.skipToPrevious();
            skipToNewMedia(playbackManager.getCurrentMediaId());
            serviceManager.notifyService();
            mediaSessionAdapter.updateAll();
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
                default: break;
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
        List<MediaBrowserCompat.MediaItem> results = mediaLibrary.getPlaylist(parent);
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
                playbackManager.setCurrentItem(mediaId);
                followingUri = mediaLibrary.getMediaUriFromMediaId(playbackManager.getNext());
                mediaPlayerAdapter.reset(uriToPlay, followingUri);

                break;
            }
        }
        if (uriToPlay == null) {
            Log.e(LOG_TAG, "failed to find requested uri in collection");
            return;
        }
        mediaSessionAdapter.updateAll();
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
        broadcastReceiver.unregisterAudioNoisyReceiver();
        mediaPlayerAdapter.pause();
        mediaSessionAdapter.updateAll();
        serviceManager.pauseService();
        //Log.i(LOG_TAG, "onPause finished");
    }

    @Override
    public void onSeekTo(long position ) {
        worker.post(() -> this.seekTo(position));

    }

    private void seekTo(long position) {
        //Log.i(LOG_TAG, "seekTO");
        mediaPlayerAdapter.seekTo(position);
        mediaSessionAdapter.updatePlaybackState(ACTION_SEEK_TO);
    }

    @Override
    public synchronized void onAddQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> this.addQueueItem(description));
    }

    private void addQueueItem(MediaDescriptionCompat description) {
        //Log.i(LOG_TAG, "addQueueItem");
        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
        mediaSessionAdapter.setQueue(item);
    }

    @Override
    public synchronized void onRemoveQueueItem(MediaDescriptionCompat description) {
        worker.post(() -> this.removeQueueItem(description));
    }
    private void removeQueueItem(MediaDescriptionCompat description) {
        MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(description, description.hashCode());
        mediaSessionAdapter.setQueue(item);
    }

    @Override
    public void onSetRepeatMode (int repeatMode) {
        worker.post(() -> this.setRepeatMode(repeatMode));
    }

    private void setRepeatMode(int repeatMode) {
        //Log.i(LOG_TAG, "set Repeat mode");
        logRepeatMode(repeatMode, LOG_TAG);
        mediaPlayerAdapter.updateRepeatMode(repeatMode);
        playbackManager.setRepeating(repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE);

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
        playbackManager.setShuffle(shuffleOn);
        String nextSongId = playbackManager.getNext();
        Uri nextUri = mediaLibrary.getMediaUriFromMediaId(nextSongId);
        mediaPlayerAdapter.setNextMediaPlayer(nextUri);
        Bundle bundle = new Bundle();
        bundle.putInt(SHUFFLE_MODE, shuffleMode);
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
                playbackManager.notifyPlaybackComplete(); // increments queue
                String nextUriToPrepare = playbackManager.getNext(); // gets uri after newly incremented index
                if (null != nextUriToPrepare) {
                    Uri nextItemUri = mediaLibrary.getMediaUriFromMediaId(nextUriToPrepare);
                    mediaPlayerAdapter.onComplete(nextItemUri);
                    mediaSessionAdapter.updateAll();
                }
            }
            serviceManager.notifyService();
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
            case INCREASE_PLAYBACK_SPEED: mediaPlayerAdapter.increaseSpeed(0.05f);
                break;
            case DECREASE_PLAYBACK_SPEED: mediaPlayerAdapter.decreaseSpeed(0.05f);
                break;
            default: break;
        }
        mediaSessionAdapter.updateAll();
    }



    private void skipToNewMedia(String newMediaId) {
        Uri newUri = mediaLibrary.getMediaUriFromMediaId(newMediaId);
        Uri followingUri = mediaLibrary.getMediaUriFromMediaId(playbackManager.getNext());
        PlaybackStateCompat currentState = mediaSessionAdapter.getCurrentPlaybackState(ACTION_SEEK_TO);
        mediaPlayerAdapter.reset(newUri, followingUri);
        if (currentState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            mediaPlayerAdapter.play();
        }
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mediaSessionAdapter.updateAll(NO_ACTION);
    }

    private MediaPlayerAdapterBase createMediaPlayerAdapter(Context context) {
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.M:
                return new MarshmallowMediaPlayerAdapterBase(context, this, this);
            case Build.VERSION_CODES.N:
                return new NougatMediaPlayerAdapterBase(context, this, this);
            default: return new OreoPlayerAdapterBase(context, this, this);
        }
    }
}