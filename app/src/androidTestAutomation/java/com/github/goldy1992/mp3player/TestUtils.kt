package com.github.goldy1992.mp3player

object TestUtils {

    private val packageName : String = "com.github.goldy1992.mp3player.automation"
    private val packageSeparator : String = ":"

    fun resourceId(id : String) : String {
        return packageName + packageSeparator + "id/" + id
    }
}