package com.example.mike.mp3player;

public class MikesMp3Player extends MikesMp3PlayerBase {
    /**
     * Use this method to set up all of the dagger dependencies before the main activity is created
     */
    @Override
    public void onCreate() {
        super.onCreate();
        setupServiceComponent(getApplicationContext());
    }


}
