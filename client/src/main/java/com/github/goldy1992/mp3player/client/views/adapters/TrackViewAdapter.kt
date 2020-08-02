package com.github.goldy1992.mp3player.client.views.adapters

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat.QueueItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.views.viewholders.MediaPlayerTrackViewHolder
import java.util.*
import javax.inject.Inject

class TrackViewAdapter

    @Inject
    constructor(val albumArtPainter: AlbumArtPainter,
                val mediaControllerAdapter: MediaControllerAdapter)
    : RecyclerView.Adapter<MediaPlayerTrackViewHolder>(), Observer<List<QueueItem>> {

    private var queue : List<QueueItem>? = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlayerTrackViewHolder { // create a new views
        val context : Context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater
                .inflate(R.layout.view_holder_media_player, parent, false)
        return MediaPlayerTrackViewHolder(v, albumArtPainter, context)
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
     * Called when the data is changed.
     * @param t  The new data
     */
    override fun onChanged(t: List<QueueItem>?) {
        this.queue = t
        notifyDataSetChanged()
    }
}