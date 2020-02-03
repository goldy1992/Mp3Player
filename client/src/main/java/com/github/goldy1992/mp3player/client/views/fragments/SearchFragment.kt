package com.github.goldy1992.mp3player.client.views.fragments

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment

import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : Fragment(), LogTagger {

    private var inputMethodManager: InputMethodManager? = null

    @Inject
    lateinit var componentClassMapper : ComponentClassMapper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        initialiseDependencies()
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        // Get the SearchView and set the searchable configuration
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(context!!, componentClassMapper.searchResultActivity!!)
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchableInfo)
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true)
        searchView.setBackgroundColor(Color.WHITE)
        val background = linearLayout.getBackground()
        background.alpha = 200
        linearLayout.setOnClickListener(View.OnClickListener { onClickOnLayout() })
        searchView.setOnClickListener(View.OnClickListener { Log.i(logTag(), "hit search view") })
        searchView.requestFocusFromTouch()
        inputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        searchView.setOnQueryTextFocusChangeListener(OnFocusChangeListener { _: View?, queryTextFocused: Boolean -> onFocusChange(queryTextFocused) })
    }

    @VisibleForTesting
    fun onClickOnLayout() {
        Log.i(logTag(), "hit on click listener")
        inputMethodManager!!.hideSoftInputFromWindow(getView()!!.windowToken, 0)
        this.parentFragmentManager.popBackStack()
    }

    @VisibleForTesting
    fun onFocusChange(queryTextFocused: Boolean) {
        Log.i("tag", "focus changed: has focus: $queryTextFocused")
        if (!queryTextFocused) {
            val fragmentManager = parentFragmentManager
            if (!fragmentManager.isDestroyed) {
                fragmentManager.popBackStack()
            }
        }
    }

    override fun logTag(): String {
        return "SRCH_FRAGMENT"
    }

    fun initialiseDependencies() {
        val component = (activity as MediaActivityCompat?)!!.mediaActivityCompatComponent
                .searchFragmentSubcomponent()
        component.inject(this)
    }
}