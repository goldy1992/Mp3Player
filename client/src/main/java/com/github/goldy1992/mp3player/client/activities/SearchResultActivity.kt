package com.github.goldy1992.mp3player.client.activities

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.media.MediaBrowserCompat
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.databinding.ActivitySearchResultsBinding
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.views.adapters.SearchResultAdapter
import com.github.goldy1992.mp3player.commons.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultActivity : MediaActivityCompat(),
 LogTagger, ItemSelectedListener {

    private var currentQuery: String? = null

    private val viewModel : SearchResultActivityViewModel by viewModels()

    private lateinit var binding : ActivitySearchResultsBinding

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter

    @Inject
    lateinit var componentClassMapper: ComponentClassMapper

    override fun onConnected() {
        super.onConnected()
        mediaBrowserAdapter.registerSearchResultListener(viewModel)
        handleIntent(intent)
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return setOf(this)
    }

    public override fun initialiseView(): Boolean {
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = searchResultAdapter

        val itemTouchListener = MyGenericItemTouchListener(applicationContext, this)
        recyclerView.addOnItemTouchListener(itemTouchListener)
        itemTouchListener.parentView = recyclerView
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(applicationContext, componentClassMapper.searchResultActivity!!)
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        //    Assumes current activity is the searchable activity

        val searchView = binding.searchView
        searchView.setSearchableInfo(searchableInfo)
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        searchView.isSubmitButtonEnabled = true
        searchView.setBackgroundColor(Color.WHITE)
        subscribeUi(searchResultAdapter)
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
            binding.searchView.setQuery(currentQuery, false)
            mediaBrowserAdapter.search(currentQuery, null)
        }
    }

    override fun logTag(): String {
        return "SRCH_RSLT_ACTVTY"
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }

    private fun subscribeUi(adapter: SearchResultAdapter) {
        viewModel.searchResults.observe(this) { result ->
            adapter.submitList(result)
        }
    }
}