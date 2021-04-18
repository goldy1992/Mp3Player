package com.github.goldy1992.mp3player.client

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.github.tmurakami.dexopener.DexOpener
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        DexOpener.install(this); // Call me first!
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

}