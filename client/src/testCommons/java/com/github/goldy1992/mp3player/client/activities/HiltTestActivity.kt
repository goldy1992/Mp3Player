package com.github.goldy1992.mp3player.client.activities

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.goldy1992.mp3player.client.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint(AppCompatActivity::class)
class HiltTestActivity : Hilt_HiltTestActivity()
{

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater: MenuInflater = this.menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)
        return false
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return false
    }
}