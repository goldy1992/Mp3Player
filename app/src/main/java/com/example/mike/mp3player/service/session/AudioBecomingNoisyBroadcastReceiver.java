package com.example.mike.mp3player.service.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import androidx.annotation.VisibleForTesting;

import javax.inject.Inject;

public class AudioBecomingNoisyBroadcastReceiver extends BroadcastReceiver {

    private MediaSessionCallback mediaSessionCallback;
    private final Context context;
    private boolean audioNoisyReceiverRegistered = false;

    /**O
     * Constructor
     */
    @Inject
    public AudioBecomingNoisyBroadcastReceiver(Context context) {
        this.context = context;
    }
    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        final boolean intentIsAudioBecomingNoisy =
                AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction());
        if (intentIsAudioBecomingNoisy) {
            mediaSessionCallback.onPause();
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

    public void setMediaSessionCallback(MediaSessionCallback mediaSessionCallback) {
        this.mediaSessionCallback = mediaSessionCallback;
    }
    @VisibleForTesting
    public boolean isAudioNoisyReceiverRegistered() {
        return audioNoisyReceiverRegistered;
    }
}
