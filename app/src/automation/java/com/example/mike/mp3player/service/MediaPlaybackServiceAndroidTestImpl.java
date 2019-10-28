package com.example.mike.mp3player.service;

import com.example.mike.mp3player.dagger.components.AndroidTestServiceComponent;
import com.example.mike.mp3player.dagger.components.DaggerAndroidTestServiceComponent;
import androidx.test.rule.GrantPermissionRule;


import org.junit.Rule;

public class MediaPlaybackServiceAndroidTestImpl extends MediaPlaybackService {

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Override
    public void onCreate() {
        initialiseDependencies();
        super.onCreate();
    }

    /**
     * TO BE CALLED BEFORE SUPER CLASS
     */
    @Override
    void initialiseDependencies() {
        AndroidTestServiceComponent component = DaggerAndroidTestServiceComponent
                .factory()
                .create(getApplicationContext(), this, "MEDIA_PLYBK_SRVC_WKR");
        component.inject(this);
    }
}