package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.concurrent.ArrayBlockingQueue;

public class MediaPlayerPool {

    private static final String LOG_TAG = "MDIA_PLYR_POOL";
    private final int QUEUE_CAPACITY = 4;
    private ArrayBlockingQueue<MediaPlayer> queue;
    private Handler worker;
    private Uri currentUri;
    private final Context context;

    public MediaPlayerPool(Context context, Looper looper) {
        this.context = context;
        worker = new Handler(looper);
        queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY, true);
    }

    public void reset(Uri uri) {
        this.currentUri = uri;
        queue.clear();
        for (int i=1; i <= QUEUE_CAPACITY; i++) {
            addMediaPlayer();
        }
    }

    public MediaPlayer take() {
        MediaPlayer toReturn = null;
        try {
            queue.take();
        } catch (InterruptedException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex));
        }
        addMediaPlayer();
        return toReturn;
    }

    private void addMediaPlayer() {
        worker.post(() -> queue.add(MediaPlayer.create(context, currentUri)));
    }

}
