package com.example.mike.mp3player.client.activities;

public class SplashScreenEntryActivityAndroidTestImpl extends SplashScreenEntryActivity {

    @Override
    Class<?> getMainActivityClass() {
        return MainActivityInjectorAndroidTestImpl.class;
    }
}
