package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.goldy1992.mp3player.client.activities.DrawerLayoutActivity
import com.github.goldy1992.mp3player.client.activities.MainActivity

abstract class DestinationFragment : Fragment() {

    abstract fun lockDrawerLayout() : Boolean

    protected var toolbar : Toolbar? = null

    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        if (activity is DrawerLayoutActivity) {
            val drawerLayout = activity.drawerLayout()
            if (lockDrawerLayout()) {
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }


            Log.i("some tag", "resumed!!!!")

        }

        if (this.toolbar != null) {
            val navController = this.findNavController()
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

            val appBarConfiguration = if(requireActivity() is DrawerLayoutActivity) {
                AppBarConfiguration(navController.graph, (requireActivity() as DrawerLayoutActivity).drawerLayout())
            } else {
                AppBarConfiguration(navController.graph)
            }
            toolbar?.setupWithNavController(navController, appBarConfiguration)
        }


    }
}