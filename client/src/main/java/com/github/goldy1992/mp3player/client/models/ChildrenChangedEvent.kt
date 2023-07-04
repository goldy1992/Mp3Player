package com.github.goldy1992.mp3player.client.models

import android.os.Bundle

data class ChildrenChangedEvent(
    val parentId: String,
    val itemCount: Int,
    val extras : Bundle =  Bundle()
) {
    companion object {
        val DEFAULT = ChildrenChangedEvent("", 1)
    }
}