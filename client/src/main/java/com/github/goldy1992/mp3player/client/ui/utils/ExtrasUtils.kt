package com.github.goldy1992.mp3player.client.ui.utils

import android.os.Bundle
import com.github.goldy1992.mp3player.commons.Constants.HAS_PERMISSIONS

object ExtrasUtils {

    fun hasPermissions(extras : Bundle) : Boolean {
        return extras.getBoolean(HAS_PERMISSIONS)
    }
}