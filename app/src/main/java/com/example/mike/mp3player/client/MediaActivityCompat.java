package com.example.mike.mp3player.client;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;

public abstract class MediaActivityCompat extends AppCompatActivity {

    public abstract void setMetaData(MediaMetadataCompat metadata);

    public abstract void setPlaybackState(PlaybackStateWrapper state);
}
