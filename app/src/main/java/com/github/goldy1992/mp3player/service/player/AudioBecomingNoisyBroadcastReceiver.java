package com.github.goldy1992.mp3player.service.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import androidx.annotation.VisibleForTesting;

import com.google.android.exoplayer2.ExoPlayer;

import javax.inject.Inject;

public class AudioBecomingNoisyBroadcastReceiver extends BroadcastReceiver {

    private final ExoPlayer player;
    private final Context context;
    private boolean audioNoisyReceiverRegistered = false;

    /**O
     * Constructor
     */
    @Inject
    public AudioBecomingNoisyBroadcastReceiver(Context context, ExoPlayer exoPlayer) {
        this.context = context;
        this.player = exoPlayer;
    }
    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        final boolean intentIsAudioBecomingNoisy =
                AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction());
        if (intentIsAudioBecomingNoisy) {
            player.setPlayWhenReady(false); // pause the player
        }
    }

    public void register() {
        if (!isRegistered()) {
            IntentFilter audioNoisyIntentFilter =
                    new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
            context.registerReceiver(this, audioNoisyIntentFilter);
            audioNoisyReceiverRegistered = true;
        }
    }

    public void unregister() {
        if (isRegistered()) {
            context.unregisterReceiver(this);
            audioNoisyReceiverRegistered = false;
        }
    }

    @VisibleForTesting
    public boolean isRegistered() {
        return audioNoisyReceiverRegistered;
    }
}