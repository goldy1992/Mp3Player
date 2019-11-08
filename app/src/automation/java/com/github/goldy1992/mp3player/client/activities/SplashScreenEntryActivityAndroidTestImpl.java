package com.github.goldy1992.mp3player.client.activities;

public class SplashScreenEntryActivityAndroidTestImpl extends SplashScreenEntryActivity {

    @Override
    Class<?> getMainActivityClass() {
        return MainActivityInjectorAndroidTestImpl.class;
    }
}
