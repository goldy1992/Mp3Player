package com.github.goldy1992.mp3player.client.dagger.modules

import android.os.Handler
import android.os.HandlerThread
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class HandlerThreadModule {
    @Provides
    fun provideHandlerThread(workerId: String?): HandlerThread {
        val handlerThread = HandlerThread(workerId)
        handlerThread.start()
        return handlerThread
    }

    @Named("worker")
    @Provides
    fun providesHandler(handlerThread: HandlerThread): Handler {
        return Handler(handlerThread.looper)
    }
}