package com.github.goldy1992.mp3player.client.listeners

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import javax.inject.Inject

class MyDrawerListener

    @Inject
    constructor() : DrawerListener {

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerOpened(drawerView: View) {}
    override fun onDrawerClosed(drawerView: View) {}
    override fun onDrawerStateChanged(newState: Int) {}
}