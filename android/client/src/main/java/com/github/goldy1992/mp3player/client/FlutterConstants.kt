package com.github.goldy1992.mp3player.client

import android.content.Context
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import io.flutter.embedding.engine.loader.FlutterLoader
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import javax.inject.Inject

@ComponentScope
class FlutterConstants

@Inject
constructor(context: Context) {

    companion object {
        const val connect = "connect"
        const val subscribe = "subscribe"
        const val request = "request"
        const val APP_PROPERTIES_ASSET = "assets/AppProperties.yml"
        const val METHOD_CHANNEL = "method-channel"
        const val CONNECTION = "connection"
        const val REQUEST = "request"
        const val SUBSCRIPTION = "subscription"
        const val PREFIX = "prefix"
        const val SUFFIXES = "suffixes"
    }


    private val CHANNEL_PREFIX : String
    private val CHANNEL_NAME_MAP : Map<String, String>

    val CONNECTION_CHANNEL_NAME : String
    val REQUEST_CHANNEL_NAME : String
    val SUBSCRIPTION_CHANNEL_NAME : String

    val APP_PROPERTIES : Map<String, Any>


    init {

        val assetLookupKey =  FlutterLoader.getInstance().getLookupKeyForAsset(APP_PROPERTIES_ASSET)
        val inputStream: InputStream = context.assets.open(assetLookupKey)

        val myYaml  = Yaml()
        APP_PROPERTIES = myYaml.load(inputStream)
        println(APP_PROPERTIES)
        val methodChannel :  Map<String, Any> = APP_PROPERTIES[METHOD_CHANNEL] as Map<String, Any>
        CHANNEL_PREFIX = methodChannel[PREFIX] as String
        CHANNEL_NAME_MAP = methodChannel[SUFFIXES] as Map<String, String>

        CONNECTION_CHANNEL_NAME = CHANNEL_NAME_MAP[CONNECTION] ?: error("")
        REQUEST_CHANNEL_NAME = CHANNEL_NAME_MAP[REQUEST] ?: error("")
        SUBSCRIPTION_CHANNEL_NAME = CHANNEL_NAME_MAP[SUBSCRIPTION] ?: error("")


    }
}