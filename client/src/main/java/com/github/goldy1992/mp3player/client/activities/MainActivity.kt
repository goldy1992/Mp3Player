package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.databinding.ActivityMainBinding
import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
import com.github.goldy1992.mp3player.client.viewmodels.MainFragmentViewModel
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class MainActivity : MediaActivityCompat()
{

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper
    override fun initialiseView(): Boolean {

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return Collections.singleton(this)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }

    public override fun onResume() {
        super.onResume()
      }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
         //       drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.action_search -> {
                Log.i(logTag(), "hit action search")
//                supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragmentContainer, searchFragment!!, "SEARCH_FGMT")
//                    .addToBackStack(null)
//                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                    .commit()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    // MediaBrowserConnectorCallback
    override fun onConnected() {
        super.onConnected()
    //    mediaBrowserAdapter.subscribeToRoot(viewModel)
    }

    @VisibleForTesting
    fun onNavigationItemSelected(menuItem: MenuItem): Boolean { // set item as selected to persist highlight
        menuItem.isChecked = true
        // close drawer when item is tapped
   //     drawerLayout!!.closeDrawers()
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true
    }




    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

}