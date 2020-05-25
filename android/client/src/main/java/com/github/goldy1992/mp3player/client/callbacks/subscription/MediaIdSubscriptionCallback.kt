package com.github.goldy1992.mp3player.client.callbacks.subscription

import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.VisibleForTesting
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.goldy1992.mp3player.client.FlutterConstants
import com.github.goldy1992.mp3player.client.MediaBrowserResponseListener
import com.github.goldy1992.mp3player.commons.*
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType
import io.flutter.plugin.common.MethodChannel
import org.apache.commons.collections4.CollectionUtils.isNotEmpty
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MediaIdSubscriptionCallback

    @Inject
    constructor(@Named(FlutterConstants.subscribe) private val methodChannel: MethodChannel) : MediaBrowserCompat.SubscriptionCallback(), LogTagger {

    private val objectMapper : ObjectMapper = ObjectMapper()

    private val mediaBrowserResponseListeners: MutableMap<String, MutableSet<MediaBrowserResponseListener>> = HashMap()

    override fun onChildrenLoaded(parentId: String, children: List<MediaBrowserCompat.MediaItem>) {

        when(getMediaItemType(children)) {
            MediaItemType.ROOT -> sendRootItems(parentId, children)
            else -> sendMetadata(parentId, children)
        }

    }

    private fun getMediaItemType(children: List<MediaBrowserCompat.MediaItem>) : MediaItemType? {
        if (isNotEmpty(children)) {
            val mediaItem : MediaBrowserCompat.MediaItem = children.first()
            return MediaItemUtils.getMediaItemType(mediaItem)
        }
        return null
    }

    private fun sendRootItems(id : String, children : List<MediaBrowserCompat.MediaItem>) {
        val rootItemsOrdered = TreeSet(compareRootMediaItemsByMediaItemType)
        rootItemsOrdered.addAll(children)
        val rootItems : ArrayList<RootItem> = ArrayList()
        for (item in rootItemsOrdered) {
            val rootItem : RootItem = RootItem.getRootItem(item)
            rootItems.add(rootItem)
        }
        val toSend : String = objectMapper.writeValueAsString(rootItems)
        val arguments : HashMap<String, String> = HashMap()
        arguments["id"] = id
        arguments["children"] = toSend

        methodChannel.invokeMethod("onChildrenLoaded", arguments)
    }

    private fun sendMetadata(id : String, children : List<MediaBrowserCompat.MediaItem>) {
        val metadataList : ArrayList<Metadata> = ArrayList()
        for (item in children) {
            val metadata : Metadata = Metadata.getMetadata(item)
            metadataList.add(metadata)
        }
        val toSend : String = objectMapper.writeValueAsString(metadataList)
        val arguments : HashMap<String, String> = HashMap()
        arguments["id"] = id
        arguments["children"] = toSend

        methodChannel.invokeMethod("onChildrenLoaded", arguments)
    }

    @Synchronized
    fun registerMediaBrowserResponseListener(key: String, listener: MediaBrowserResponseListener?) {
        if (mediaBrowserResponseListeners[key] == null) {
            mediaBrowserResponseListeners[key] = HashSet()
        }
        mediaBrowserResponseListeners[key]!!.add(listener!!)
    }

    @VisibleForTesting
    fun getMediaBrowserResponseListeners(): Map<String, MutableSet<MediaBrowserResponseListener>> {
        return mediaBrowserResponseListeners
    }

    override fun logTag(): String {
        return "SUBSCRIPTION_CALLBACK"
    }
}