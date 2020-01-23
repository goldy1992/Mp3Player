package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.dagger.components.DaggerSplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.client.dagger.components.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class SplashScreenEntryActivityInjectorTestImpl : SplashScreenEntryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        val componentClassMapper = ComponentClassMapper.Builder()
                .service(SplashScreenEntryActivityInjectorTestImpl::class.java)
                .mainActivity(TestMainActivity::class.java)
                .build()
        val component: SplashScreenEntryActivityComponent = DaggerSplashScreenEntryActivityComponent
                .factory()
                .create(this, this, componentClassMapper)
        component.inject(this)
    }
}