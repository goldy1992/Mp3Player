package com.github.goldy1992.mp3player.client.activities

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.goldy1992.mp3player.LogTagger
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.callbacks.search.SearchResultListener
import com.github.goldy1992.mp3player.client.views.adapters.SearchResultAdapter
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import kotlinx.android.synthetic.main.activity_search_results.*
import javax.inject.Inject

abstract class SearchResultActivity : MediaActivityCompat(), SearchResultListener, LogTagger, ItemSelectedListener {

    private var currentQuery: String? = null
    private var searchResultAdapter: SearchResultAdapter? = null

    override val workerId: String
        get() = "SRCH_RSLT_ACTVTY"

    override fun onConnected() {
        super.onConnected()
        initialiseView(R.layout.activity_search_results)
        mediaBrowserAdapter?.registerSearchResultListener(this)
        handleIntent(intent)
    }

    public override fun initialiseView(layoutId: Int): Boolean {
        setContentView(layoutId)
        setSupportActionBar(toolbar)

        recyclerView.setAdapter(searchResultAdapter)
        val itemTouchListener = MyGenericItemTouchListener(applicationContext, this)
        recyclerView.addOnItemTouchListener(itemTouchListener)
        itemTouchListener.parentView =recyclerView
        recyclerView.setLayoutManager(LinearLayoutManager(applicationContext))
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(applicationContext, SearchResultActivityInjector::class.java)
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        //    Assumes current activity is the searchable activity
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        searchView.setSearchableInfo(searchableInfo)
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        searchView.isSubmitButtonEnabled = true
        searchView.setBackgroundColor(Color.WHITE)
        return true
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        if (null != item) {
            var intentClass: Class<*>? = null
            val mediaItemType = MediaItemUtils.getMediaItemType(item)
            when (mediaItemType) {
                MediaItemType.SONG, MediaItemType.SONGS -> mediaControllerAdapter!!.playFromMediaId(MediaItemUtils.getLibraryId(item), null)
                MediaItemType.FOLDER, MediaItemType.FOLDERS -> intentClass = FolderActivityInjector::class.java
                else -> {
                }
            }
            if (null != intentClass) {
                val intent = Intent(applicationContext, intentClass)
                intent.putExtra(Constants.MEDIA_ITEM, item)
                startActivity(intent)
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            currentQuery = intent.getStringExtra(SearchManager.QUERY)
            if (null != searchView) {
                searchView!!.setQuery(currentQuery, false)
            }
            mediaBrowserAdapter!!.search(currentQuery, null)
        }
    }

    override fun onSearchResult(searchResults: List<MediaBrowserCompat.MediaItem>?) {
        searchResultAdapter!!.items.clear()
        searchResultAdapter!!.items.addAll(searchResults!!)
        searchResultAdapter!!.notifyDataSetChanged()
        Log.i(logTag, "received search results")
    }

    override fun getLogTag(): String {
        return "SRCH_RSLT_ACTVTY"
    }

    @Inject
    fun setSearchResultAdapter(searchResultAdapter: SearchResultAdapter?) {
        this.searchResultAdapter = searchResultAdapter
    }

}