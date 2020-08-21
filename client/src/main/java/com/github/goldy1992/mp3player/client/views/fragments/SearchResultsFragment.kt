package com.github.goldy1992.mp3player.client.views.fragments

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.SearchResultsFragmentViewModel
import com.github.goldy1992.mp3player.client.databinding.FragmentSearchResultsBinding
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.views.adapters.SearchResultAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.lang3.StringUtils.isNotEmpty
import javax.inject.Inject


@AndroidEntryPoint
class SearchResultsFragment : DestinationFragment(), MyGenericItemTouchListener.ItemSelectedListener, LogTagger {

    val queryListener : QueryListener = QueryListener()

    @Inject
    lateinit var mediaBrowserAdapter: MediaBrowserAdapter

    @Inject
    lateinit var mediaControllerAdapter : MediaControllerAdapter

    @Inject
    lateinit var searchResultAdapter: SearchResultAdapter

    private lateinit var inputMethodManager: InputMethodManager

    private val viewModel : SearchResultsFragmentViewModel by viewModels()

    override fun lockDrawerLayout(): Boolean {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        this.inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        setHasOptionsMenu(true)
        val binding = FragmentSearchResultsBinding.inflate(inflater)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = searchResultAdapter
        val itemTouchListener = MyGenericItemTouchListener(requireContext(), this)
        recyclerView.addOnItemTouchListener(itemTouchListener)
        itemTouchListener.parentView = recyclerView
        val context : Context = requireContext()
        recyclerView.layoutManager = LinearLayoutManager(context)
        // Assumes current activity is the searchable activity
        setUpToolbar(binding.toolbar)

        subscribeUi(searchResultAdapter)

        return binding.root
    }

    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        if (null != item) {
            when (MediaItemUtils.getMediaItemType(item)) {
                MediaItemType.SONG, MediaItemType.SONGS -> mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null)
                MediaItemType.FOLDER, MediaItemType.FOLDERS -> {
                    val action = MainFragmentDirections.goToFolderFragment(item)
                    findNavController().navigate(action)
                }
                else -> return
            }
            inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_results_menu, menu)
        configureSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPause() {
        super.onPause()
        Log.i(logTag(), "on pause")
        hideKeyboard()
    }

    override fun onResume() {
        super.onResume()
        Log.i(logTag(), "on resume")
        showKeyboard()
    }

    private fun configureSearchView(searchMenu: Menu) {
        val item = searchMenu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        searchView.queryHint = "Search Media"
        val magId = resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<View>(magId) as ImageView
        magImage.layoutParams = LinearLayout.LayoutParams(0, 0)

        // Get the SearchView and set the searchable configuration
        val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(context, requireActivity().javaClass)
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView.setOnQueryTextListener(queryListener)
        searchView.setSearchableInfo(searchableInfo)
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        searchView.isSubmitButtonEnabled = true
        searchView.requestFocus()
    }

    inner class QueryListener : SearchView.OnQueryTextListener {
        /** {@inheritDoc}  */
        override fun onQueryTextSubmit(query: String?): Boolean {
            return performSearch(query = query)        }

        /** {@inheritDoc}  */
        override fun onQueryTextChange(newText: String?): Boolean {
            return performSearch(query = newText)
        }

        private fun performSearch(query: String?): Boolean {
            if (isNotEmpty(query)) {
                mediaBrowserAdapter.search(query, null)
            }
            return true
        }

    }

    private fun subscribeUi(adapter: SearchResultAdapter) {
        mediaBrowserAdapter.registerSearchResultListener(viewModel)
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "SRCH_RSLTS_FRGMNT"
    }


    private fun hideKeyboard() {
        try {
            val activity : Activity = requireActivity()
            val view : View? = activity.currentFocus
            if (view?.windowToken != null) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showKeyboard() {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, HIDE_IMPLICIT_ONLY)
    }

}