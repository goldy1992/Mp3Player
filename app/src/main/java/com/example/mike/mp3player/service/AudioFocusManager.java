package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.AudioManager;

import androidx.media.AudioAttributesCompat;
import androidx.media.AudioFocusRequestCompat;
import androidx.media.AudioManagerCompat;

public class AudioFocusManager
 implements AudioManager.OnAudioFocusChangeListener {

    public static final float MEDIA_VOLUME_DEFAULT = 1.0f;
    private static final float MEDIA_VOLUME_DUCK = 0.2f;

    MyMediaPlayerAdapter player;
    AudioManager audioManager;
    Context context;

    boolean playWhenAudioFocusGained = false;
    private boolean audioNoisyReceiverRegistered = false;

    public AudioFocusManager(Context context, MyMediaPlayerAdapter player) {
        this.context = context;
        this.player = player;
    }

    public void init() {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (playWhenAudioFocusGained && !player.isPlaying()) {
                    player.play();
                } else if (player.isPlaying()) {
                    player.setVolume(MEDIA_VOLUME_DEFAULT);
                }
                playWhenAudioFocusGained = false;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                player.setVolume(MEDIA_VOLUME_DUCK);
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (player.isPlaying()) {
                    playWhenAudioFocusGained = true;
                    player.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                abandonAudioFocus();
                playWhenAudioFocusGained  = false;
                player.pause();
                break;
        }
    }

    public boolean requestAudioFocus() {
        AudioAttributesCompat.Builder audioAttributesBuilder = new AudioAttributesCompat.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setUsage(AudioAttributesCompat.USAGE_MEDIA)
                .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC);
        AudioAttributesCompat audioAttributes = audioAttributesBuilder.build();

        AudioFocusRequestCompat audioFocusRequestCompat = new AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setOnAudioFocusChangeListener(this)
                .setWillPauseWhenDucked(false).build();

        int result = AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequestCompat);

        return isRequestGranted(result);
    }

    private boolean isRequestGranted(int result) {
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        }
        return false;
    }

    public void playbackPaused() {
        if (!playWhenAudioFocusGained) {
            abandonAudioFocus();
        }
    }

    private boolean abandonAudioFocus() {
        AudioFocusRequestCompat audioFocusRequestCompat = new AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(this)
                .build();
        int result = AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequestCompat);
        return  isRequestGranted(result);
    }
}
