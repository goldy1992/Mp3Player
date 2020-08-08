package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.activities.MainActivity

abstract class DestinationFragment : Fragment() {

    abstract fun lockDrawerLayout() : Boolean

    private var drawerLayout : DrawerLayout? = null

    private var activity : MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        if (activity is MainActivity) {
            this.activity = activity
            this.drawerLayout = activity.drawerLayout
            if (lockDrawerLayout()) {
                activity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                activity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
    }

    protected open fun setUpToolbar(toolbar : Toolbar) {
        val navController = this.findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        activity?.setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}