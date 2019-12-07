package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.activities.SearchResultActivity
import com.github.goldy1992.mp3player.client.dagger.components.DaggerTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class SearchResultActivityInjectorTestImpl : SearchResultActivity() {
    public override fun onCreate(savedInstance: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstance)
    }

    public override fun initialiseDependencies() {
        val componentClassMapper : ComponentClassMapper = ComponentClassMapper.Builder()
                .folderActivity(FolderActivityInjectorTestImpl::class.java)
                .mediaPlayerActivity(MediaPlayerActivityInjectorTestImpl::class.java)
                .service(SearchResultActivityInjectorTestImpl::class.java)
                .searchResultActivity(SearchResultActivityInjectorTestImpl::class.java)
                .build()
        val component: MediaActivityCompatComponent = DaggerTestMediaActivityCompatComponent
                .factory()
                .create(applicationContext, workerId, this, componentClassMapper)
        mediaActivityCompatComponent = component
        component.searchResultActivitySubComponent().inject(this)
    }
}