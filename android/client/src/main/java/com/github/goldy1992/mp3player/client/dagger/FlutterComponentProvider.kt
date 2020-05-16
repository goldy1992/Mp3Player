package com.github.goldy1992.mp3player.client.dagger

import android.content.Context
import com.github.goldy1992.mp3player.client.dagger.components.FlutterMediaActivityComponent
import io.flutter.embedding.engine.FlutterEngine

interface FlutterComponentProvider {

    fun flutterMediaActivityComponent(context: Context,
    flutterEngine: FlutterEngine): FlutterMediaActivityComponent
}