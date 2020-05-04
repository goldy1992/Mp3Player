package com.github.goldy1992.mp3player.client.activities

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.search.SearchResultListener
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.views.adapters.SearchResultAdapter
import com.github.goldy1992.mp3player.commons.*
import kotlinx.android.synthetic.main.activity_search_results.*
import kotlinx.android.synthetic.main.activity_search_results.searchView
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet


class SearchResultActivity : MediaActivityCompat(), SearchResultListener, LogTagger, ItemSelectedListener {

    private var currentQuery: String? = null
    private lateinit var searchResultAdapter: SearchResultAdapter

    private lateinit var inputMethodManager: InputMethodManager

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    override fun onConnected() {
        super.onConnected()
        mediaBrowserAdapter.registerSearchResultListener(this)
        handleIntent(intent)
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        val toReturn : MutableSet<MediaBrowserConnectionListener> = HashSet()
        toReturn.add(this)
        return toReturn
    }

    public override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_search_results)
        setSupportActionBar(toolbar)
        inputMethodManager = applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        recyclerView.adapter = searchResultAdapter
        val itemTouchListener = MyGenericItemTouchListener(applicationContext, this)
        recyclerView.addOnItemTouchListener(itemTouchListener)
        itemTouchListener.parentView =recyclerView
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(applicationContext, componentClassMapper.searchResultActivity!!)
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        //    Assumes current activity is the searchable activity
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        searchView.setSearchableInfo(searchableInfo)
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        searchView.isSubmitButtonEnabled = true
        searchView.setBackgroundColor(Color.WHITE)
        searchView.requestFocusFromTouch()
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        return true
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        if (null != item) {
            var intentClass: Class<*>? = null
            when (MediaItemUtils.getMediaItemType(item)) {
                MediaItemType.SONG, MediaItemType.SONGS -> mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null)
                MediaItemType.FOLDER, MediaItemType.FOLDERS -> intentClass = componentClassMapper.folderActivity
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
//            if (null != searchView) {
//                searchView!!.setQuery(currentQuery, false)
//            }
            mediaBrowserAdapter.search(currentQuery, null)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
     //   menuInflater.inflate(R.menu.search_bar_menu, menu)

        return true
    }
    override fun onSearchResult(searchResults: List<MediaBrowserCompat.MediaItem>?) {
        searchResultAdapter.items.clear()
        searchResultAdapter.items.addAll(searchResults!!)
        searchResultAdapter.notifyDataSetChanged()
        Log.i(logTag(), "received search results")
    }

    override fun logTag(): String {
        return "SRCH_RSLT_ACTVTY"
    }

    @Inject
    fun setSearchResultAdapter(searchResultAdapter: SearchResultAdapter) {
        this.searchResultAdapter = searchResultAdapter
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }

    override fun initialiseDependencies() {
        super.initialiseDependencies()
        this.mediaActivityCompatComponent.inject(this)
    }
}