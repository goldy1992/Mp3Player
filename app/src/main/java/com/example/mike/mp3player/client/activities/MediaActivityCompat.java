package com.example.mike.mp3player.client.activities;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.MediaControllerAdapter;

import androidx.appcompat.app.AppCompatActivity;

public abstract class MediaActivityCompat extends AppCompatActivity {

    private MediaControllerAdapter mediaControllerAdapter;

//    public abstract void setMetaData(MediaMetadataCompat metadata);

//    public abstract void setPlaybackState(PlaybackStateCompat state);

    public abstract MediaControllerAdapter getMediaControllerAdapter();
}
