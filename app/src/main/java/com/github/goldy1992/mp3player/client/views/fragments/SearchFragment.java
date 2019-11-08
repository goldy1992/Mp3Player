package com.github.goldy1992.mp3player.client.views.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.github.goldy1992.mp3player.LogTagger;
import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.activities.SearchResultActivityInjector;

public class SearchFragment extends Fragment implements LogTagger {

    private LinearLayout linearLayout;
    private SearchView searchView;
    private InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {

        this.searchView = view.findViewById(R.id.search_view);
        this.inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);

         // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getContext(), SearchResultActivityInjector.class);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setBackgroundColor(Color.WHITE);


        this.linearLayout = view.findViewById(R.id.search_fragment_layout);
        Drawable background = this.linearLayout.getBackground();
        background.setAlpha(200);
        linearLayout.setOnClickListener(this::onClickOnLayout);

        searchView.setOnClickListener((View v) -> {
            Log.i(getLogTag(), "hit search view");
        });

        searchView.requestFocusFromTouch();
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        searchView.setOnQueryTextFocusChangeListener(this::onFocusChange);
    }

    @VisibleForTesting
    public void onClickOnLayout(View view) {
        Log.i(getLogTag(), "hit on click listener");
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0 );
        getFragmentManager().popBackStack();
    }


    @VisibleForTesting
    public void onFocusChange(View v, boolean queryTextFocused) {
        Log.i("tag", "focus changed: has focus: " + queryTextFocused);
        if (!queryTextFocused) {
            getFragmentManager().popBackStack();
        }
    }
    @Override
    public String getLogTag() {
        return "SRCH_FRAGMENT";
    }
}
