package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import androidx.annotation.VisibleForTesting
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.databinding.ActivityMainBinding
import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.google.android.material.navigation.NavigationView
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

    lateinit var drawerLayout: DrawerLayout

    lateinit var navigationView: NavigationView

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
        this.drawerLayout = binding.drawerLayout
        drawerLayout.addDrawerListener(myDrawerListener)
        this.navigationView = binding.navigationView
        initNavigationView()

        setContentView(binding.root)

        val navController : NavController = findNavController(R.id.nav_host_container)
        binding.navigationView.setupWithNavController(navController)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }

    public override fun onResume() {
        super.onResume()
      }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
////        menuInflater.inflate(R.menu.menu_main, menu)
////        return super.onCreateOptionsMenu(menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController : NavController = findNavController(R.id.nav_host_container)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.i(logTag(), "hit on options item selected")
//        val navController = findNavController(R.id.nav_host_container)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

//        when (item.itemId) {
//            android.R.id.home -> {
//               // drawerLayout!!.openDrawer(GravityCompat.START)
//                return true
//            }
//            R.id.action_search -> {
//                Log.i(logTag(), "hit action search")
////                supportFragmentManager
////                    .beginTransaction()
////                    .add(R.id.fragmentContainer, searchFragment!!, "SEARCH_FGMT")
////                    .addToBackStack(null)
////                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
////                    .commit()
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//        return true
    //}

    @VisibleForTesting
    fun onNavigationItemSelected(menuItem: MenuItem): Boolean { // set item as selected to persist highlight
        menuItem.isChecked = true
        // close drawer when item is tapped
        drawerLayout.closeDrawers()
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true
    }

    private fun initNavigationView() {
        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem -> onNavigationItemSelected(menuItem) }
        val spinner = navigationView.menu.findItem(R.id.themes_menu_item).actionView as Spinner
        //    ThemeSpinnerController(requireContext(), spinner, this, componentClassMapper)
    }


    override fun logTag(): String {
        return "MAIN_ACTIVITY"
    }

}