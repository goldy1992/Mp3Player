package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.databinding.ActivityMainBinding
import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class MainActivity : MediaActivityCompat()
{
    @Inject
    lateinit var myDrawerListener: MyDrawerListener

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper



    override fun initialiseView(): Boolean {
        return true
    }

    /**
     * @return A set of MediaBrowserConnectionListeners to be connected to.
     */
    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return emptySet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            // navigate to media player fragment
//            trackToPlay = intent.data
        }


        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
               // drawerLayout!!.openDrawer(GravityCompat.START)
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



    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

}