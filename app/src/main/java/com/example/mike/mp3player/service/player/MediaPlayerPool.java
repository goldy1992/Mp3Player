package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MediaPlayerPool {

    private static final String WORKER_ID = "M_PLYR_POOL_WRKR";
    private static final String LOG_TAG = "MDIA_PLYR_POOL";
    private static final long POLL_TIMEOUT = 1000L;
    public static final int DEFAULT_QUEUE_CAPACITY = 4;
    private ArrayBlockingQueue<MediaPlayer> queue;
    private Uri currentUri;
    private final Context context;
    private final int queueCapacity;

    public MediaPlayerPool(Context context) {
        this(context, DEFAULT_QUEUE_CAPACITY);
    }

    public MediaPlayerPool(Context context, final int queueCapacity) {
        this.context = context;
        this.queueCapacity = queueCapacity;
        setQueue(new ArrayBlockingQueue<>(queueCapacity, true));
    }

    public void reset(Uri uri) {
        Log.i(LOG_TAG, "hit reset");
        this.currentUri = uri;
        getQueue().clear();
        for (int i = 1; i <= queueCapacity; i++) {
            AsyncTask.execute(this::addMediaPlayer);
        }
    }

    public MediaPlayer take() {
        MediaPlayer toReturn = null;
        if (isSet()) {
            try {
                toReturn = getQueue().poll(POLL_TIMEOUT, TimeUnit.MILLISECONDS);
                addMediaPlayer();
            } catch (InterruptedException ex) {
                Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex));
                Thread.currentThread().interrupt();
            } catch (IllegalStateException ex) {
                Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex));
            }

        }
        return toReturn;
    }

    private void addMediaPlayer() {
        if (isSet()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, currentUri);
            if (!getQueue().add(mediaPlayer)) {
                Log.e(LOG_TAG, "failed to add mediaplayer: " + mediaPlayer + " to the queue");
            }
        }
    }

    public boolean isSet() {
        return currentUri != null;
    }

    @VisibleForTesting
    public ArrayBlockingQueue<MediaPlayer> getQueue() {
        return queue;
    }

    public void setQueue(ArrayBlockingQueue<MediaPlayer> queue) {
        this.queue = queue;
    }
}
