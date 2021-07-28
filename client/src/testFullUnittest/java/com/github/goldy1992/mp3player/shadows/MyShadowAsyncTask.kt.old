package com.github.goldy1992.mp3player.shadows

import android.app.Application
import android.os.AsyncTask
import androidx.test.core.app.ApplicationProvider
import org.robolectric.Shadows
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowAsyncTask

@Implements(AsyncTask::class)
class MyShadowAsyncTask<Params, Progress, Result>  : ShadowAsyncTask<Params, Progress, Result>() {
    fun execute(runnable: Runnable?) {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val shadowApplication = Shadows.shadowOf(application)
        shadowApplication.foregroundThreadScheduler.post(runnable)
    }
}