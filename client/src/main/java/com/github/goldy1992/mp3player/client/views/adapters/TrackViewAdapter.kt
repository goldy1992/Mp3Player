package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.MediaSessionCompat.QueueItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.queue.QueueListener
import com.github.goldy1992.mp3player.client.views.viewholders.MediaPlayerTrackViewHolder
import java.util.*

class TrackViewAdapter // TODO: if there is no queue make an empty view holder
(val albumArtPainter: AlbumArtPainter,
val mediaControllerAdapter: MediaControllerAdapter)
    : RecyclerView.Adapter<MediaPlayerTrackViewHolder>(), QueueListener {

    private var queue : List<QueueItem>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlayerTrackViewHolder { // create a new views
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater
                .inflate(R.layout.view_holder_media_player, parent, false)
        return MediaPlayerTrackViewHolder(v, albumArtPainter)
    }

    override fun onBindViewHolder(holder: MediaPlayerTrackViewHolder, position: Int) {
        val queueItem = queue!![position]
        holder.bindMediaItem(queueItem)
    }

    override fun getItemCount(): Int {
        return if (null != queue) {
            queue!!.size
        } else -1
    }

    /**
     *
     */
    fun updateQueue(updatedQueue: List<QueueItem>) {
        this.queue = updatedQueue
         notifyDataSetChanged()
    }

    override fun onQueueChanged(queue: List<QueueItem>) {
        this.queue = queue
    }

    init {
        mediaControllerAdapter.registerListener(this)
    }



}