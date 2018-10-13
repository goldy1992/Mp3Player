package com.example.mike.mp3player.service;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
/**
 * Created by Mike on 24/09/2017.
 */

public class MediaSessionCallback extends MediaSessionCompat.Callback {

    private ServiceManager serviceManager;
    private MyMediaPlayerAdapter myMediaPlayerAdapter;


    public MediaSessionCallback(Context context, PlayBackNotifier playBackNotifier, MetaDataNotifier metaDataNotifier, ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.myMediaPlayerAdapter = new MyMediaPlayerAdapter(context, playBackNotifier, metaDataNotifier);
        this.myMediaPlayerAdapter.init();
    }

    @Override
    public void onPlay() {
        myMediaPlayerAdapter.play();
        serviceManager.startService();
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle bundle) {
        super.onPrepareFromUri(uri, bundle);
        myMediaPlayerAdapter.prepareFromUri(uri);
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle bundle) {
        super.onPlayFromUri(uri, bundle);
        myMediaPlayerAdapter.playFromUri(uri);
        serviceManager.startMediaSession();
    }

    @Override
    public void onStop() {
        serviceManager.stopService();
        myMediaPlayerAdapter.stop();
    }

    @Override
    public void onPause() {
        myMediaPlayerAdapter.pause();
        // unregister BECOME_NOISY BroadcastReceiver
//        unregisterReceiver(myNoisyAudioStreamReceiver, intentFilter);
        // Take the serviceManager out of the foreground, retain the notification
        serviceManager.pauseService();
    }

    @Override
    public void onPrepare() {
//        if (!mediaSession.isActive()) {
//            mediaSession.setActive(true);
//        } // if session active
    } // onPrepare

    @Override
    public void onSeekTo(long position ) {
        myMediaPlayerAdapter.seekTo(position);
    }
}