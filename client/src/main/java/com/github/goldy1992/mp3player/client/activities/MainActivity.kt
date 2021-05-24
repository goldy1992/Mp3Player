package com.github.goldy1992.mp3player.client.activities

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.drawerlayout.widget.DrawerLayout
import com.github.goldy1992.mp3player.client.ui.ComposeApp
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

/**
 * The Main Activity
 */
@AndroidEntryPoint(MediaActivityCompat::class)
open class MainActivity : Hilt_MainActivity(),
    CoroutineScope by GlobalScope,
    DrawerLayoutActivity
{

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    lateinit var drawerLayout: DrawerLayout

    lateinit var navigationView: NavigationView

    lateinit var mediaRepository : MediaRepository

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set


    override fun initialiseView(): Boolean {
        return true
    }

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @InternalCoroutinesApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMediaRepository()
        setContent {
            ComposeApp(mediaRepository, mediaBrowserAdapter, mediaControllerAdapter)
        }
    }


    @VisibleForTesting
    fun onNavigationItemSelected(menuItem: MenuItem): Boolean { // set item as selected to persist highlight
        menuItem.isChecked = true
        // close drawer when item is tapped
        drawerLayout.closeDrawers()
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true
    }

    private fun initMediaRepository() {
        mediaRepository = MediaRepository(mediaBrowserAdapter.subscribeToRoot())
        mediaRepository.rootItems.observe(this) {
            for (mediaItem in it) {
                val id = MediaItemUtils.getMediaId(mediaItem)!!
                mediaRepository.itemMap[MediaItemUtils.getRootMediaItemType(mediaItem)!!] = mediaBrowserAdapter.subscribe(id)
            }
        }
    }

    private fun initNavigationView() {
        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem -> onNavigationItemSelected(menuItem) }
    }

    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

    override fun drawerLayout(): DrawerLayout? {
        return drawerLayout
    }

}