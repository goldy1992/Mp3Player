package com.github.goldy1992.mp3player.client.utils

import android.os.Bundle
import com.github.goldy1992.mp3player.commons.Constants

object ExtrasUtils {

    fun hasPermissions(extras : Bundle) : Boolean {
        return extras.getBoolean(Constants.HAS_PERMISSIONS)
    }
}