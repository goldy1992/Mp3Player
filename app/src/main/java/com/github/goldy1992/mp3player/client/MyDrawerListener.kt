package com.github.goldy1992.mp3player.client

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener

class MyDrawerListener : DrawerListener {
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerOpened(drawerView: View) {}
    override fun onDrawerClosed(drawerView: View) {}
    override fun onDrawerStateChanged(newState: Int) {}
}