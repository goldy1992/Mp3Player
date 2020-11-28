package com.github.goldy1992.mp3player.client.views.fragments

import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.activities.MainActivity

abstract class DestinationFragment : Fragment() {

    abstract fun lockDrawerLayout() : Boolean

    private var drawerLayout : DrawerLayout? = null

    private var activity : MainActivity? = null

    protected var toolbar : Toolbar? = null

    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        if (activity is MainActivity) {
            this.activity = activity
            this.drawerLayout = activity.drawerLayout
            if (lockDrawerLayout()) {
                activity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                activity.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            if (this.toolbar != null) {
                val navController = this.findNavController()
                activity.setSupportActionBar(toolbar)
                toolbar?.setupWithNavController(navController, activity.appBarConfiguration)
            }
        }

        Log.i("some tag", "resumed!!!!")
    }
}