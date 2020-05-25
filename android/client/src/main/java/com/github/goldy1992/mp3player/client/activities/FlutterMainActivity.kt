package com.github.goldy1992.mp3player.client.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import com.github.goldy1992.mp3player.client.*
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.client.dagger.FlutterComponentProvider
import com.github.goldy1992.mp3player.client.dagger.components.FlutterMediaActivityComponent
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class FlutterMainActivity : FlutterActivity(),
        MediaInterface,
        FlutterComponentProvider,
        MediaBrowserResponseListener,
        MethodChannel.MethodCallHandler
{

    @Inject
    @Named(FlutterConstants.request)
    lateinit var methodChannel: MethodChannel

    @Inject
    override lateinit var mediaBrowserAdapter: MediaBrowserAdapter
    @Inject
    override lateinit var myConnectionCallback: MyConnectionCallback
    @Inject
    override lateinit var mediaControllerAdapter: MediaControllerAdapter
    @Inject
    lateinit var componentClassMapper: ComponentClassMapper
    @Inject
    lateinit var flutterConstants: FlutterConstants


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialiseDependencies()
        methodChannel.setMethodCallHandler(this)
     }
    @CallSuper
    override fun initialiseDependencies() {
        val component = flutterMediaActivityComponent(applicationContext, flutterEngine!!)
        component.inject(this)
    }

    override fun flutterMediaActivityComponent(context: Context, flutterEngine: FlutterEngine): FlutterMediaActivityComponent {
        return (applicationContext as FlutterComponentProvider).flutterMediaActivityComponent(context, flutterEngine)
    }


    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return setOf(this)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return setOf(this)
    }

    override fun logTag(): String {
        return "FLUTTER_MAIN_ACTIVITY"
    }

    override fun onConnected() {
        super.onConnected()
        mediaBrowserAdapter.registerRootListener(this)
        methodChannel.invokeMethod("onConnected", null)

    }


    override fun onChildrenLoaded(parentId: String, children: ArrayList<MediaBrowserCompat.MediaItem>) {
        val rootItemsOrdered = TreeSet(compareRootMediaItemsByMediaItemType)
        rootItemsOrdered.addAll(children)
        var rootItems : MutableList<String> = ArrayList()
        for (mediaItem in rootItemsOrdered) {
            val id = MediaItemUtils.getMediaId(mediaItem)!!
            Log.i(logTag(), "media id: $id")
            val category = MediaItemUtils.getExtra(Constants.ROOT_ITEM_TYPE, mediaItem) as MediaItemType
            rootItems.add(category.title)
        }

        methodChannel.invokeMethod("onReceiveTabNames", rootItems)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

        when (call.method) {

            "subscribe" -> {
                mediaBrowserAdapter.subscribe(call.arguments as String)
                result.success(null)
            }

            "connect" -> {
                connect()
                result.success(null)
            }
        }
    }

}
