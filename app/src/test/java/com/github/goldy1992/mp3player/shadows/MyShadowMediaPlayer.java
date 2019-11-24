package com.github.goldy1992.mp3player.shadows;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowMediaPlayer;
import org.robolectric.shadows.util.DataSource;

@Implements(MediaPlayer.class)
public class MyShadowMediaPlayer extends ShadowMediaPlayer {

    private PlaybackParams playbackParams;

    @Implementation
    public void __constructor__() {
        super.__constructor__();
    }

    @Implementation
    public static MediaPlayer create(Context context, Uri uri){
        DataSource ds = DataSource.toDataSource(String.valueOf(uri.getPath()));
        addMediaInfo(ds, new ShadowMediaPlayer.MediaInfo());

        MediaPlayer mp = new MediaPlayer();
        MyShadowMediaPlayer shadow = Shadow.extract(mp);
        try {
            shadow.setDataSource(ds);
            shadow.setState(ShadowMediaPlayer.State.INITIALIZED);
            PlaybackParams playbackParams = new PlaybackParams();
            playbackParams.setPitch(0f);
            playbackParams.setSpeed(0);
            playbackParams.setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
            shadow.setPlaybackParams(playbackParams);

            mp.prepare();
        } catch (Exception e) {
            return null;
        }

        return mp;
    }

    @Implementation
    public void setPlaybackParams(PlaybackParams playbackParams){
        this.playbackParams = playbackParams;
    }

    @Implementation
    public PlaybackParams getPlaybackParams(){
        return playbackParams;
    }
}
