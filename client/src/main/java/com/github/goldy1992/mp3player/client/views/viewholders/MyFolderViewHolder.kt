package com.github.goldy1992.mp3player.client.views.viewholders

import android.support.v4.media.MediaBrowserCompat
import android.view.View
import android.widget.TextView
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.MediaItemUtils

class MyFolderViewHolder(itemView: View, albumArtPainter: AlbumArtPainter?) : MediaItemViewHolder(itemView, albumArtPainter) {
    private val folderName: TextView
    private val folderPath: TextView
    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {
        val folderNameText = extractFolderName(item)
        folderName.text = folderNameText
        val folderPathText = extractFolderPath(item)
        folderPath.text = folderPathText
    }

    private fun extractFolderName(song: MediaBrowserCompat.MediaItem): String {
        return MediaItemUtils.getDirectoryName(song)
    }

    private fun extractFolderPath(song: MediaBrowserCompat.MediaItem): String {
        return MediaItemUtils.getDirectoryPath(song)
    }

    init {
        folderName = itemView.findViewById(R.id.folderName)
        folderPath = itemView.findViewById(R.id.folderPath)
    }
}