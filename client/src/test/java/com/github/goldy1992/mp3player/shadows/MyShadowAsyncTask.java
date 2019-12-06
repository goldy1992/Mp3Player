package com.github.goldy1992.mp3player.shadows;

import android.app.Application;
import android.os.AsyncTask;

import androidx.test.core.app.ApplicationProvider;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowAsyncTask;

import static org.robolectric.Shadows.shadowOf;

@Implements(AsyncTask.class)
public class MyShadowAsyncTask extends ShadowAsyncTask {

    public static void execute(Runnable runnable) {
        Application application = ApplicationProvider.getApplicationContext();
        ShadowApplication shadowApplication = shadowOf(application);
        shadowApplication.getForegroundThreadScheduler().post(runnable);
    }

}
