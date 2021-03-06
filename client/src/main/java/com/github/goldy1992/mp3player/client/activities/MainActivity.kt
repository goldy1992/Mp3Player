package com.github.goldy1992.mp3player.client.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.client.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    lateinit var myDrawerListener: MyDrawerListener

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    lateinit var drawerLayout: DrawerLayout

    lateinit var navigationView: NavigationView

    @get:VisibleForTesting
    var trackToPlay: Uri? = null
        private set

    override fun initialiseView(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag(), "onCreate called")
        val binding : ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        this.drawerLayout = binding.drawerLayout
        drawerLayout.addDrawerListener(myDrawerListener)
        this.navigationView = binding.navigationView
        initNavigationView()
        setContentView(binding.root)

        val navController : NavController = findNavController(R.id.nav_host_container)
        binding.navigationView.setupWithNavController(navController)

        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {

            trackToPlay = intent.data
            launch(Dispatchers.Default) {
                mediaControllerAdapter.playFromUri(trackToPlay, null)
            }
            navController.navigate(R.id.go_to_media_player)
            // navigate to media player fragment
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController : NavController = findNavController(R.id.nav_host_container)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
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