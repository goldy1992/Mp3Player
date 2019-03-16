package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.concurrent.ArrayBlockingQueue;

public class MediaPlayerPool {

    private static final String WORKER_ID = "M_PLYR_POOL_WRKR";
    private static final String LOG_TAG = "MDIA_PLYR_POOL";
    private final int QUEUE_CAPACITY = 4;
    private ArrayBlockingQueue<MediaPlayer> queue;
    private Uri currentUri;
    private final Context context;

    public MediaPlayerPool(Context context) {
        this.context = context;
        queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY, true);
    }

    public void reset(Uri uri) {
        Log.i(LOG_TAG, "hit reset");
        this.currentUri = uri;
        queue.clear();
        for (int i=1; i <= QUEUE_CAPACITY; i++) {
            AsyncTask.execute(() -> this.addMediaPlayer());
        }
    }

    public MediaPlayer take() {
        MediaPlayer toReturn = null;
        try {
            toReturn = queue.take();
        } catch (InterruptedException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex));
        }
        addMediaPlayer();
        return toReturn;
    }

    private void addMediaPlayer() {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, currentUri);
        queue.add(mediaPlayer);
    }
}
