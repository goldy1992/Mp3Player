package com.example.mike.mp3player;

import android.app.Application;

import com.example.mike.mp3player.service.library.DaggerMediaLibraryComponent;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.MediaLibraryComponent;

public class MikesMp3Player extends Application {
    MediaLibraryComponent mediaLibraryComponent;
    @Override
    public void onCreate() {
        super.onCreate();

    }

    void inject(MediaLibrary mediaLibrary) {

    }



}
