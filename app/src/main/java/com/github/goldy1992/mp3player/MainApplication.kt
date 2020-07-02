package com.github.goldy1992.mp3player


import android.app.Application
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.dagger.components.AppComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application()