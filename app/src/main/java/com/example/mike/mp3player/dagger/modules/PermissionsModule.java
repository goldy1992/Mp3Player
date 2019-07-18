package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.PermissionGranted;
import com.example.mike.mp3player.client.PermissionsProcessor;
import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionsModule {

    @Provides
    public PermissionsProcessor providePermission(SplashScreenEntryActivity activity,
                                                  PermissionGranted permissionGranted) {
        return new PermissionsProcessor(activity, permissionGranted);
    }
}
