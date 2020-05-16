package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.FlutterConstants
import com.github.goldy1992.mp3player.client.FlutterConstants.Companion.connect
import com.github.goldy1992.mp3player.client.FlutterConstants.Companion.request
import com.github.goldy1992.mp3player.client.FlutterConstants.Companion.subscribe
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Module
import dagger.Provides
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import javax.inject.Named

@Module
class FlutterModule {

    @Provides
    @Named(connect)
    @ComponentScope
    fun provideConnectionMethodChannel(flutterEngine: FlutterEngine,
                                       flutterConstants: FlutterConstants)
            : MethodChannel {
        return MethodChannel(flutterEngine.dartExecutor.binaryMessenger,
                flutterConstants.CONNECTION_CHANNEL_NAME as String?)
    }

    @Provides
    @Named(subscribe)
    @ComponentScope
    fun provideSubscriptionMethodChannel(flutterEngine: FlutterEngine,
                                         flutterConstants: FlutterConstants)
            : MethodChannel {
        return MethodChannel(flutterEngine.dartExecutor.binaryMessenger,
                flutterConstants.SUBSCRIPTION_CHANNEL_NAME as String?)
    }

    @Provides
    @Named(request)
    @ComponentScope
    fun provideRequestMethodChannel(flutterEngine: FlutterEngine,
                                    flutterConstants: FlutterConstants)
            : MethodChannel {
        return MethodChannel(flutterEngine.dartExecutor.binaryMessenger,
                flutterConstants.REQUEST_CHANNEL_NAME as String?)
    }
}