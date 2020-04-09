package com.github.goldy1992.mp3player

import com.github.goldy1992.mp3player.dagger.components.DaggerAutomationAppComponent

class AndroidTestMainApplication : MainApplication() {

    override fun initialiseDependencies() {
        this.appComponent = DaggerAutomationAppComponent
                .factory()
                .create(componentClassMapper)
    }

}