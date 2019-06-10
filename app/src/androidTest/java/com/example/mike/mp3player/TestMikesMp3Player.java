package com.example.mike.mp3player;

import android.util.Log;

import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MockMediaRetriever;

public class TestMikesMp3Player extends MikesMp3PlayerBase {

    private final String LOG_TAG = "TestMkeMp3App";

    /**
     * Use this method to set up all of the dagger dependencies before the main activity is created
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "using test mp3 application");
        MediaRetriever mockMediaRetriever = new MockMediaRetriever(getApplicationContext());
        setupMediaLibrary(mockMediaRetriever);

    }
}
