package com.github.goldy1992.mp3player.client.views.viewholders

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.widget.TextView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.client.databinding.FolderItemMenuBinding

class MyFolderViewHolder

    constructor(context : Context,
                binding: FolderItemMenuBinding,
                albumArtPainter: AlbumArtPainter?)

    : MediaItemViewHolder<FolderItemMenuBinding>(context, binding, albumArtPainter) {

    private val folderName: TextView = binding.folderName
    private val folderPath: TextView = binding.folderPath

    override fun bindMediaItem(item: MediaBrowserCompat.MediaItem) {
        val folderNameText = extractFolderName(item)
        folderName.text = folderNameText
        val folderPathText = extractFolderPath(item)
        folderPath.text = folderPathText
    }

    private fun extractFolderName(song: MediaBrowserCompat.MediaItem): String? {
        return MediaItemUtils.getDirectoryName(song)
    }

    private fun extractFolderPath(song: MediaBrowserCompat.MediaItem): String? {
        return MediaItemUtils.getDirectoryPath(song)
    }

}