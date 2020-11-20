package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.activities.MainActivity

abstract class DestinationFragment : Fragment(), LifecycleEventObserver {

    abstract fun lockDrawerLayout() : Boolean

    private var drawerLayout : DrawerLayout? = null

    private var activity : MainActivity? = null

    protected var toolbar : Toolbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().lifecycle.addObserver(this)
    }

    protected open fun setUpToolbar(toolbar : Toolbar) {
     }

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

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_START) {

        }
    }
}