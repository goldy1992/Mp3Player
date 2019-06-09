package com.example.mike.mp3player;

import android.util.Log;

import com.example.mike.mp3player.service.library.mediaretriever.EmptyMediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

public class UnitTestMikesMp3Player extends MikesMp3PlayerBase {

    private static final String LOG_TAG = "UnitTestMkeMp3App";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "using UNIT test mp3 application");
        MediaRetriever mockMediaRetriever = new EmptyMediaRetriever(getApplicationContext());
        setupMediaLibrary(mockMediaRetriever);

    }
}
