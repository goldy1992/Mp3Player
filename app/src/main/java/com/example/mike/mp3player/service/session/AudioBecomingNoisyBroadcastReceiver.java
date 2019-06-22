package com.example.mike.mp3player.service.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import androidx.annotation.VisibleForTesting;

import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.NO_ACTION;

public class AudioBecomingNoisyBroadcastReceiver extends BroadcastReceiver {

    private MediaPlayerAdapterBase mediaPlayerAdapter;
    private final MediaSessionAdapter mediaSessionAdapter;
    private final Context context;
    private ServiceManager serviceManager;
    private boolean audioNoisyReceiverRegistered = false;

    /**
     * Constructor
     */
    @Inject
    public AudioBecomingNoisyBroadcastReceiver(Context context, MediaSessionAdapter mediaSessionAdapter,
                           MediaPlayerAdapterBase mediaPlayerAdapter, ServiceManager serviceManager) {
        this.context = context;
        this.mediaSessionAdapter = mediaSessionAdapter;
        this.serviceManager = serviceManager;
        this.mediaPlayerAdapter = mediaPlayerAdapter;
    }
    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            if (mediaPlayerAdapter.isPlaying()) {
                mediaPlayerAdapter.pause();
                mediaSessionAdapter.updateAll(NO_ACTION);
                serviceManager.notifyService();
            }
        }
    }

    public void registerAudioNoisyReceiver() {
        if (!isAudioNoisyReceiverRegistered()) {
            IntentFilter audioNoisyIntentFilter =
                    new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
            context.registerReceiver(this, audioNoisyIntentFilter);
            audioNoisyReceiverRegistered = true;
        }
    }

    public void unregisterAudioNoisyReceiver() {
        if (isAudioNoisyReceiverRegistered()) {
            context.unregisterReceiver(this);
            audioNoisyReceiverRegistered = false;
        }
    }
    @VisibleForTesting
    public boolean isAudioNoisyReceiverRegistered() {
        return audioNoisyReceiverRegistered;
    }
}
