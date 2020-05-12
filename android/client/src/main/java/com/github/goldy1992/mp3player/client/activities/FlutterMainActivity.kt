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
import com.github.goldy1992.mp3player.client.dagger.ClientComponentsProvider
import com.github.goldy1992.mp3player.client.dagger.components.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FlutterMainActivity : FlutterActivity(), ClientComponentsProvider, MediaInterface, MediaBrowserResponseListener, MethodChannel.MethodCallHandler, EventChannel.StreamHandler, EventChannel.EventSink {

    val CHANNEL : String = "MY_METHOD_CHANNEL"

    lateinit var methodChannel: MethodChannel

    lateinit var eventChannel: EventChannel
    @Inject
    override lateinit var mediaBrowserAdapter: MediaBrowserAdapter
    @Inject
    override lateinit var myConnectionCallback: MyConnectionCallback
    @Inject
    override lateinit var mediaControllerAdapter: MediaControllerAdapter
    @Inject
    lateinit var componentClassMapper: ComponentClassMapper
    override lateinit var mediaActivityCompatComponent: MediaActivityCompatComponent
    @CallSuper
    override fun initialiseDependencies() {
        val component = getClientsComponentProvider()
                .mediaActivityComponent(applicationContext, this)
        this.mediaActivityCompatComponent = component
        component.inject(this)
    }

    fun getClientsComponentProvider() : ClientComponentsProvider {
        return (applicationContext as ClientComponentsProvider)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)

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

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        this.methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            methodChannel.setMethodCallHandler(this)

        this.eventChannel = EventChannel(flutterEngine.dartExecutor.binaryMessenger, "CONNECTION_CHANNEL")
        this.eventChannel.setStreamHandler(this)
        // Note: this method is invoked on the main thread.
            // TODO
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

            "SUBSCRIBE_ROOT" -> {
                mediaBrowserAdapter.subscribeToRoot()
                result.success(null)
            }

            "CONNECT" -> {
                connect()
                result.success(null)
            }
        }
    }

    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity, permissionGranted: PermissionGranted): SplashScreenEntryActivityComponent {
        TODO("Not yet implemented")
    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectionListener): MediaActivityCompatComponent {
        TODO("Not yet implemented")
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        TODO("Not yet implemented")
    }

    override fun onCancel(arguments: Any?) {
        TODO("Not yet implemented")
    }

    override fun endOfStream() {
        TODO("Not yet implemented")
    }

    override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
        TODO("Not yet implemented")
    }

    override fun success(event: Any?) {
        TODO("Not yet implemented")
    }

}
