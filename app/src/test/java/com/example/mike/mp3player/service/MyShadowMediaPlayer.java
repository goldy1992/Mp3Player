package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

import static org.robolectric.shadows.ShadowMediaPlayer.State.INITIALIZED;
@Implements(MediaPlayer.class)
public class MyShadowMediaPlayer extends ShadowMediaPlayer {

    @Implementation
    protected static MediaPlayer create(Context context, Uri uri) {
        MediaPlayer mp = new MediaPlayer();
        ShadowMediaPlayer shadow = Shadow.extract(mp);
        try {
            shadow.setState(INITIALIZED);
            shadow.setDataSource(DataSource.toDataSource(context, uri));
            mp.prepare();
        } catch (Exception e) {
            return null;
        }

        return mp;
    }
}
