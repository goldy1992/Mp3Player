package com.example.mike.mp3player;

import com.example.mike.mp3player.service.library.mediaretriever.ContentResolverMediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

public class MikesMp3Player extends MikesMp3PlayerBase {
    /**
     * Use this method to set up all of the dagger dependencies before the main activity is created
     */
    @Override
    public void onCreate() {
        super.onCreate();
        MediaRetriever contentResolverMediaRetriever = new ContentResolverMediaRetriever(getApplicationContext());
        setupMediaLibrary(contentResolverMediaRetriever);
    }


}
