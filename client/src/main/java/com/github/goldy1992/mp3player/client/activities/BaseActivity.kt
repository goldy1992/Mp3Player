package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.github.goldy1992.mp3player.commons.DependencyInitialiser
import com.github.goldy1992.mp3player.commons.LogTagger

/**
 * Base class that serves as a Util to help perform Dagger dependency injection.
 */
abstract class BaseActivity : AppCompatActivity(), LogTagger {

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}