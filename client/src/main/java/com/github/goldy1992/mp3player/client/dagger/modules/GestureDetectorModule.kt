package com.github.goldy1992.mp3player.client.dagger.modules

import android.content.Context
import android.view.GestureDetector
import androidx.core.view.GestureDetectorCompat
import com.github.goldy1992.mp3player.client.listeners.MyGestureListener
import dagger.Module
import dagger.Provides

@Module
class GestureDetectorModule {

    @Provides
    fun providesGestureDetectorCompat(context : Context,
                                      myGestureListener: MyGestureListener) : GestureDetectorCompat {
        return GestureDetectorCompat(context, myGestureListener)
    }
}