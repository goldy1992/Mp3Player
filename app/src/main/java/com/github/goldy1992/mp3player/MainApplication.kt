package com.github.goldy1992.mp3player


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
    }
}